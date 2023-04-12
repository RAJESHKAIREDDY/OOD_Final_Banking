package transactions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.CreditCardsDAO;
import dao.DatabaseConnectionFactory;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.CCBillPaymentStatus;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.CreditCard;
import models.Transaction;
import models.User;

public class CCBillPaymentTransaction extends Thread {

	private String userId;
	private String accountId;
	private String cardId;
	private double accountBalance;
	private double remainingCreditLimit;
	private double amount;
	private PaymentFromAccountTransaction accountPaymentTransaction;
	
    public CCBillPaymentTransaction(
    		String userId, 
    		String accountId, 
    		String cardId, 
    		double accountBalance,
			double remainingCreditLimit, 
			double amount) {
		
    	super();
		this.userId = userId;
		this.accountId = accountId;
		this.cardId = cardId;
		this.accountBalance = accountBalance;
		this.remainingCreditLimit = remainingCreditLimit;
		this.amount = amount;
	}

	public void payCCBillAmount() throws Exception {
        
		accountPaymentTransaction = 
        		new PaymentFromAccountTransaction(
        				userId, 
        				accountId, 
        				accountBalance, 
        				amount,
        				TransactionCategory.CC_BILL_PAYMENT);
		
        accountPaymentTransaction.start();
       
        updateRemainingCreditLimit(userId, cardId, remainingCreditLimit, amount);
    }

    public static CCBillPaymentStatus getCCBillPaymentStatus(
    		User user, CreditCard creditCard) throws Exception {
        double totalCreditLimit = creditCard.getTotalCreditLimit();
        double remainingBalance = creditCard.getRemainingCreditLimit();
        double totalPayableAmount = totalCreditLimit - remainingBalance;
        Date lastDueDate = TransactionsDAO.getLastDueDate(user.getUserId().toString());
        Date lastPaymentDate = creditCard.getLastPaymentDate();
        CCBillPaymentStatus paymentStatus;
        if(lastPaymentDate == null) {
        	if (lastDueDate == null)
            	paymentStatus = CCBillPaymentStatus.NO_TRANSACTIONS;
        	else {
        		int compareValue = new Date().compareTo(lastDueDate);
        		if(compareValue > 0)
        			paymentStatus = CCBillPaymentStatus.LATE_YET_PENDING;
        		else
        			paymentStatus = CCBillPaymentStatus.PENDING;
        	}
        }
        else {
        	int comparedDates = lastPaymentDate.compareTo(lastDueDate);
            
            if (comparedDates > 0) {
            	//late payment
            	if (totalPayableAmount > 0)
                	paymentStatus = CCBillPaymentStatus.LATE_YET_PENDING;
                else
                	paymentStatus = CCBillPaymentStatus.LATE;
            }
            else {
            	
            	//in time
                if (totalPayableAmount > 0)
                	paymentStatus = CCBillPaymentStatus.PENDING;
                else
                	paymentStatus = CCBillPaymentStatus.IN_TIME;
            }
        }
        return paymentStatus;
    }
    
    private static void updateRemainingCreditLimit(
    		String userId, 
    		String cardId, 
    		double remainingCreditLimit, 
    		double amount) throws Exception {
    	
        remainingCreditLimit += amount;
        CreditCardsDAO.updateRemainingCreditLimit(userId, cardId, remainingCreditLimit);
        
        Timestamp lastPaymentDate = new Timestamp(new Date().getTime());
        CreditCardsDAO.updateLastPaymentDate(userId, cardId, lastPaymentDate);
        
//        Transaction transaction = new Transaction();
//		transaction.setTransactionId(UUID.randomUUID());
//		transaction.setTransactionCategory(TransactionCategory.CC_BILL_PAYMENT);
//		transaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
//		transaction.setTransactionMode(TransactionMode.DEBIT);
//		transaction.setTransactionName("Credit Card bill Payment");
//		transaction.setAmount(amount);
//		TransactionsDAO.createNewTransaction(userId, transaction);
		
		User user = UsersDAO.getUserByEmail(userId);
		CreditCard creditCard = CreditCardsDAO.getCreditCard(cardId);
		int creditScore = user.getCreditScore();
		CCBillPaymentStatus paymentStatus = 
				CCBillPaymentTransaction.getCCBillPaymentStatus(user, creditCard);
		
		TransactionsDAO.updateTransactionsPaymentStatus(userId);
		
		if(paymentStatus == CCBillPaymentStatus.LATE)
			user.setCreditScore(creditScore - 20);
		else if(paymentStatus == CCBillPaymentStatus.IN_TIME)
			user.setCreditScore(creditScore + 40);
		UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
    }
    
    @Override
    public void run() {
    	// TODO Auto-generated method stub
    	synchronized (this) {
    		try {
    			payCCBillAmount();
    		} catch (Exception exception) {
    			// TODO Auto-generated catch block
    			Logger.getLogger(CCBillPaymentTransaction.class.getName())
    			.log(Level.SEVERE, null, exception);
    		}
		}
    }
}
