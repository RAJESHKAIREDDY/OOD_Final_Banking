package transactions;

import validations.TransactionValidations;

import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.DatabaseConnectionFactory;
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

public class PaymentFromAccountTransaction extends Thread {

    private double amount;
    private double accountBalance;
    private String userId;
    private String accountId;
    private PaymentStatus paymentStatus;
    private boolean isSuccessful;
    private String statusMessage;
    private TransactionCategory transactionCategory;
    public PaymentFromAccountTransaction(
    		String userId, 
    		String accountId, 
    		double accountBalance, 
    		double amount,
    		TransactionCategory transactionCategory) {
        this.amount = amount;
        this.userId = userId;
        this.accountId = accountId;
        this.accountBalance = accountBalance;
        this.paymentStatus = PaymentStatus.PENDING;
        this.transactionCategory = transactionCategory;
    }

    private final Payment accountPayment = (double amount) -> {
   
        Scanner sc = new Scanner(System.in);
		int iterateCountForInvalid = 0;
		int iterateCountForIncorrect = 0;
		int iterateCountForInsufficient = 0;
        while(!paymentStatus.equals(PaymentStatus.SUCCESS)) {
        	System.out.println("*** Enter OTP for this transaction ***");
        	int generatedOTP = TransactionUtils.generateOTP();
        	Date generatedOTPTimestamp = new Date();
        	User user = UsersDAO.getUserById(userId);
        	String toEmail = user.getEmail();
        	EmailService.sendEmail(toEmail, "OTP for Account Payment Transaction", "Your OTP for this transaction is "+generatedOTP);
            String OTP = sc.nextLine();
            boolean isValidOTP = TransactionValidations.isOTPValid(OTP, generatedOTPTimestamp);
            System.out.println("IS OTP VALID : "+isValidOTP);
            if(isValidOTP) {
                int enteredOTP = Integer.parseInt(OTP);
                if(enteredOTP == generatedOTP) {
                	
                    if(amount <= accountBalance) {
                    	accountBalance -= amount;
                        System.out.println("\n\n*** Paid USD " + amount + " from Savings A/C Account ID : "+accountId+" ***");
                        System.out.println("Updated Balance : USD "+accountBalance);
                        SavingsAccountsDAO.updateAccountBalance(userId, accountId, accountBalance);
                        SavingsAccount userAccount = SavingsAccountsDAO.getSavingsAccountById(accountId);
                        Transaction transaction = new Transaction();
                        transaction.setTransactionId(UUID.randomUUID());
                        transaction.setTransactionCategory(transactionCategory);
                        transaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
                        transaction.setTransactionMode(TransactionMode.DEBIT);
                        transaction.setAccountNumber(userAccount.getAccountNumber());
                        transaction.setTransactionName("Account Transaction");
                        transaction.setAmount(amount);
                        transaction.setAccountNumber(userAccount.getAccountNumber());
                        if(amount > 0)
                        	TransactionsDAO.createNewTransaction(userId, transaction);
                        setPaymentStatus(PaymentStatus.SUCCESS);
                        int creditScore = user.getCreditScore();
            			user.setCreditScore(creditScore + 10);
            			UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
                    }
                    else {
                    	setPaymentStatus(PaymentStatus.INSUFFICIENT_FUNDS);
                    	if(iterateCountForInsufficient < 2) {
                    		setStatusMessage("*** Insufficient Funds. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInsufficient)+ "***");
                    		System.out.println(statusMessage);
                    		iterateCountForInsufficient++;
                        }
                        else
                        {
                            setStatusMessage("*** Transaction Blocked. Access Denied ***");
                            System.out.println(statusMessage);
                            System.exit(0);
                        }
                    }
                }
                else {
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
            }
            else {
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
            	accountPayment.payment(amount);
            	boolean isSuccessful = paymentStatus == PaymentStatus.SUCCESS;
            	setSuccessful(isSuccessful);
            }
            catch (Exception exception) {
            	Logger.getLogger(PaymentFromAccountTransaction.class.getName())
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
