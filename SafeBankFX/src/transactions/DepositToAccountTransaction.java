package transactions;

import validations.TransactionValidations;

import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.PaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.SavingsAccount;
import models.Transaction;
import models.User;
import notifications.EmailService;
import utils.TransactionUtils;

public class DepositToAccountTransaction extends Thread {

	double amount;
	double accountBalance;
	String userId;
	String accountId;
	private PaymentStatus paymentStatus;
	private boolean isSuccessful;
	private String statusMessage;

	public DepositToAccountTransaction(String userId, String accountId, double accountBalance, double amount) {
		this.amount = amount;
		this.userId = userId;
		this.accountId = accountId;
		this.accountBalance = accountBalance;
		paymentStatus = PaymentStatus.PENDING;
	}

	private final Deposit deposit = (double amount) -> {

		Scanner sc = new Scanner(System.in);
		int iterateCountForInvalid = 0;
		int iterateCountForIncorrect = 0;
		int generatedOTP = TransactionUtils.generateOTP();
		while (!paymentStatus.equals(PaymentStatus.SUCCESS)) {
	     	System.out.println("*** Enter OTP for this transaction ***");
			Date generatedOTPTimestamp = new Date();
			User user = UsersDAO.getUserById(userId);
			String toEmail = user.getEmail();
			EmailService.sendEmail(toEmail, "OTP for Account Deposit Transaction",
					"Your OTP for this transaction is " + generatedOTP);
			String OTP = sc.nextLine();
			boolean isValidOTP = TransactionValidations.isOTPValid(OTP, generatedOTPTimestamp);
			if (isValidOTP) {
				int enteredOTP = Integer.parseInt(OTP);
				if (enteredOTP == generatedOTP) {
					accountBalance += amount;
					System.out.println(
							"\n\n*** Deposited USD " + amount + " to Savings A/C Account ID : " + accountId + " ***");
					System.out.println("Updated Balance : USD " + accountBalance);
					SavingsAccountsDAO.updateAccountBalance(userId, accountId, accountBalance);
					SavingsAccount savingsAccount = SavingsAccountsDAO.getSavingsAccountById(accountId);
					Transaction transaction = new Transaction();
					transaction.setTransactionId(UUID.randomUUID());
					transaction.setTransactionCategory(TransactionCategory.CASH_DEPOSIT);
					transaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
					transaction.setTransactionMode(TransactionMode.CREDIT);
					transaction.setTransactionName("Deposit for Medical Purpose");
					transaction.setAmount(amount);
					transaction.setAccountNumber(savingsAccount.getAccountNumber());
					TransactionsDAO.createNewTransaction(userId, transaction);
					
					setPaymentStatus(PaymentStatus.SUCCESS);
					int creditScore = user.getCreditScore();
					user.setCreditScore(creditScore + 10);
					UsersDAO.updateUserCreditScore(userId, user.getCreditScore());

				} else {
					setPaymentStatus(PaymentStatus.INCORRECT_OTP);
                	if(iterateCountForIncorrect < 2) {
                		setStatusMessage("*** Incorrect OTP. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForIncorrect)+ "***");
                		System.out.println(statusMessage);
                        iterateCountForIncorrect++;
                    }
                    else
                    {
                    	setStatusMessage("*** Transaction Blocked. Access Denied ***");
                    	System.out.println(statusMessage);
                        System.exit(0);
                    }
				}
			} else {
				setPaymentStatus(PaymentStatus.INVALID_OTP);
            	if(iterateCountForInvalid < 2) {
                    setStatusMessage("\n\n*** Invalid OTP. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInvalid));
                    System.out.println(statusMessage);
                    iterateCountForInvalid++;
                }
                else
                {
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
				deposit.deposit(amount);
				boolean isSuccessful = paymentStatus == PaymentStatus.SUCCESS;
            	setSuccessful(isSuccessful);
			} catch (Exception exception) {
				Logger.getLogger(DepositToAccountTransaction.class.getName())
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
