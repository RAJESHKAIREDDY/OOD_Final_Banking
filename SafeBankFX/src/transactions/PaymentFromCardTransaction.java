package transactions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.CreditCardsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.CCBillPaymentStatus;
import enums.PaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.CreditCard;
import models.Transaction;
import models.User;
import notifications.EmailService;
import utils.CreditCardUtils;
import utils.TransactionUtils;
import validations.TransactionValidations;

public class PaymentFromCardTransaction extends Thread {

	double amount;
	double remainingCreditLimit;
	String userId;
	String cardId;
	private PaymentStatus paymentStatus;
	private boolean isSuccessful;
	private String statusMessage;

	public PaymentFromCardTransaction(
			String userId, 
			String cardId, 
			double remainingCreditLimit, 
			double amount) {
		
		this.amount = amount;
		this.userId = userId;
		this.cardId = cardId;
		this.remainingCreditLimit = remainingCreditLimit;
		paymentStatus = PaymentStatus.PENDING;
	}

	private final Payment cardPayment = (double amount) -> {

		Scanner sc = new Scanner(System.in);
		int iterateCountForInvalid = 0;
		int iterateCountForIncorrect = 0;
		int iterateCountForInsufficient = 0;
		while (!paymentStatus.equals(PaymentStatus.SUCCESS)) {
			System.out.println("*** Enter OTP for this transaction ***");
			int generatedOTP = TransactionUtils.generateOTP();
			Date generatedOTPTimestamp = new Date();
			User user = UsersDAO.getUserById(userId);
			String toEmail = user.getEmail();
			EmailService.sendEmail(toEmail, "OTP for Card Payment Transaction",
					"Your OTP for this transaction is " + generatedOTP);
			String OTP = sc.nextLine();
			boolean isValidOTP = TransactionValidations.isOTPValid(OTP, generatedOTPTimestamp);
			System.out.println("IS OTP VALID : " + isValidOTP);
			if (isValidOTP) {
				int enteredOTP = Integer.parseInt(OTP);
				if (enteredOTP == generatedOTP) {
					if (amount <= remainingCreditLimit) {
						remainingCreditLimit -= amount;
						System.out.println("\n\n*** Paid USD " + amount + " from Card ID : " + cardId + " ***");
						System.out.println("Updated Balance : USD " + remainingCreditLimit);
						Date dueDate = CreditCardUtils.generateCCDueDateAfterTwoMin(new Date());
//								CreditCardUtils.generateDueDateForCreditCard();
						CreditCardsDAO.updateRemainingCreditLimit(userId, cardId, remainingCreditLimit);
						CreditCard userCreditCard = CreditCardsDAO.getCreditCard(cardId);
						Transaction transaction = new Transaction();
						transaction.setTransactionId(UUID.randomUUID());
						transaction.setTransactionCategory(TransactionCategory.ONLINE_PAYMENT);
						transaction.setTransactionType(TransactionType.CARD_TRANSACTION);
						transaction.setTransactionMode(TransactionMode.DEBIT);
						transaction.setTransactionName("Online Payment for Shopping");
						transaction.setAmount(amount);
						transaction.setDueDate(dueDate);
						transaction.setPaymentStatus(CCBillPaymentStatus.PENDING);
						transaction.setCardNumber(userCreditCard.getCardNumber());
						TransactionsDAO.createNewTransaction(userId, transaction);
						setPaymentStatus(PaymentStatus.SUCCESS);
						int creditScore = user.getCreditScore();
						CreditCard creditCard = CreditCardsDAO.getCreditCard(cardId);
						CCBillPaymentStatus paymentStatus = 
								CCBillPaymentTransaction.getCCBillPaymentStatus(user, creditCard);
						if(paymentStatus == CCBillPaymentStatus.LATE)
							user.setCreditScore(creditScore - 20);
						else if(paymentStatus == CCBillPaymentStatus.LATE_YET_PENDING) {
							double totCreditLimit = creditCard.getTotalCreditLimit();
							double remCreditlimit = creditCard.getRemainingCreditLimit();
							Timestamp lastPaymentDateTimestamp = 
									new Timestamp(creditCard.getLastPaymentDate().getTime());
							
							Transaction lastCCBillPaymentTransaction = 
									TransactionsDAO.getTransactionByDate(userId, lastPaymentDateTimestamp);
							double amountPaid = lastCCBillPaymentTransaction.getAmount();
							if(amountPaid <= 0.1 * (totCreditLimit - (remCreditlimit - amount))) {
								user.setCreditScore(creditScore - 30);
							}
							else
								user.setCreditScore(creditScore - 20);
						}
							
						if(amount > 0.33 * remainingCreditLimit)
							user.setCreditScore(creditScore - 20);
						else
							user.setCreditScore(creditScore + 20);
						
						UsersDAO.updateUserCreditScore(userId, user.getCreditScore());

					} else {
						setPaymentStatus(PaymentStatus.INSUFFICIENT_FUNDS);
						if (iterateCountForInsufficient < 2) {
							setStatusMessage("*** Insufficient Funds. Transaction Declined. Attempts Remaining : "
									+ (2 - iterateCountForInsufficient) + "***");
							System.out.println(statusMessage);
							iterateCountForInsufficient++;
						} else {
							setStatusMessage("*** Transaction Blocked. Access Denied ***");
							System.out.println(statusMessage);
							System.exit(0);
						}
					}
				} else {
					setPaymentStatus(PaymentStatus.INCORRECT_OTP);
					if (iterateCountForIncorrect < 2) {
						setStatusMessage("*** Incorrect OTP. Transaction Declined. Attempts Remaining : "
								+ (2 - iterateCountForIncorrect) + "***");
						System.out.println(statusMessage);
						iterateCountForIncorrect++;
					} else {
						setStatusMessage("*** Transaction Blocked. Access Denied ***");
						System.out.println(statusMessage);
						System.exit(0);
					}
				}
			} else {
				setPaymentStatus(PaymentStatus.INVALID_OTP);
				if (iterateCountForInvalid < 2) {
					setStatusMessage("\n\n*** Invalid OTP. Transaction Declined. Attempts Remaining : "
							+ (2 - iterateCountForInvalid));
					System.out.println(statusMessage);
					iterateCountForInvalid++;
				} else {
					setStatusMessage("*** Transaction Blocked. Access Denied ***");
					System.out.println(statusMessage);
					System.exit(0);
				}
			}
		}
		sc.close();
	};

	@Override
	public void run() {
		synchronized (this) {
			try {
				cardPayment.payment(amount);
			} catch (Exception exception) {
				Logger.getLogger(PaymentFromCardTransaction.class.getName())
    			.log(Level.SEVERE, null, exception);
				System.out.println("\n\n*** Transaction Failed .Try Again Later ***");
			}
		}
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
