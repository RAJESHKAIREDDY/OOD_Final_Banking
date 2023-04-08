package transactions;

import java.util.Date;

import models.CreditCard;
import models.User;

public class CreditCardBillPayment extends Thread {

	private AccountPaymentTransaction accountPaymentTransaction;
    public synchronized void payTotalAmount(String userId, String accountId, double accountBalance, double amount) throws Exception {
        accountPaymentTransaction = new AccountPaymentTransaction(userId, accountId, accountBalance, amount);
        accountPaymentTransaction.start();
    }

    public synchronized void payMinimumAmount(String userId, String accountId, double accountBalance, double amount) throws Exception {
    	accountPaymentTransaction = new AccountPaymentTransaction(userId, accountId, accountBalance, amount);
        accountPaymentTransaction.start();
    }
    
    public synchronized void payCustomAmount(String userId, String accountId, double accountBalance, double amount) throws Exception {
    	accountPaymentTransaction = new AccountPaymentTransaction(userId, accountId, accountBalance, amount);
        accountPaymentTransaction.start();
    }

    public static String getCCBillPaymentStatus(User user, CreditCard creditCard) throws Exception {
        double totalCreditLimit = creditCard.getTotalCreditLimit();
        double remainingBalance = creditCard.getRemainingCreditLimit();
        double totalPayableAmount = totalCreditLimit - remainingBalance;
        Date lastDueDate = creditCard.getLastDueDate();
        Date lastPaymentDate = creditCard.getLastPaymentDate();
        int comparedDates = lastPaymentDate.compareTo(lastDueDate);
        boolean noCreditTransactionsYet = user.getCreatedAt().compareTo(lastDueDate) == 0;
        String paymentStatus = "";
        if (comparedDates > 0)
        	paymentStatus = "Late";
        else if (comparedDates == 0)
        	paymentStatus = "On Time";
        else {
            if (totalPayableAmount > 0)
            	paymentStatus = "Pending";
            else
            	paymentStatus = "In Time";
        }
        
        if (noCreditTransactionsYet)
        		return "No Transactions";
        return paymentStatus;
    }
}
