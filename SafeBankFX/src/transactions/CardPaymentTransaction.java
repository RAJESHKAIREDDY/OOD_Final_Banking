package transactions;

import java.util.Scanner;
import java.util.UUID;

import dao.CreditCardsDAO;
import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.Transaction;
import validations.ValidatePin;

public class CardPaymentTransaction extends Thread {
	
	double amount;
    double remainingCreditLimit;
    String userId;
    String cardId;
    public CardPaymentTransaction(String userId, String cardId, double remainingCreditLimit, double amount) {
        this.amount = amount;
        this.userId = userId;
        this.cardId = cardId;
        this.remainingCreditLimit = remainingCreditLimit;
    }

    private final Payment cardPayment = (double amount) -> {
   
        Scanner sc = new Scanner(System.in);
        boolean iterate = true;
        int iterateCountForInvalid = 0;
        int iterateCountForIncorrect = 0;
        while (iterate) {
        	System.out.println("*** Enter high security code ***");
            String securityCode = sc.nextLine();
            boolean isValidSecurityCode = ValidatePin.isSecurityCodeValid(securityCode);
            if(isValidSecurityCode) {
                int secCode = Integer.parseInt(securityCode);
                if(secCode == 259) {
                    remainingCreditLimit -= amount;
                    System.out.println("\n\n*** Paid USD " + amount + " from Card ID : "+cardId+" ***");
                    System.out.println("Updated Balance : USD "+remainingCreditLimit);
                    CreditCardsDAO.updateRemainingCreditLimit(userId, cardId, remainingCreditLimit);
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(UUID.randomUUID());
                    transaction.setTransactionCategory(TransactionCategory.ONLINE_PAYMENT);
                    transaction.setTransactionType(TransactionType.CARD_TRANSACTION);
                    transaction.setTransactionMode(TransactionMode.DEBIT);
                    transaction.setTransactionName("Online Payment for Shopping");
                    TransactionsDAO.createNewTransaction(userId, transaction);
                    iterate = false;
                }
                else {
                    if(iterateCountForIncorrect < 2) {
                        System.out.println("\n\n*** Incorrect Pin. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForIncorrect));
                        iterateCountForIncorrect++;
                    }
                    else
                    {
                        System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                        System.exit(0);
                    }
                }
            }
            else {
                if(iterateCountForInvalid < 2) {
                    System.out.println("\n\n*** Invalid Pin. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInvalid));
                    iterateCountForInvalid++;
                }
                else
                {
                    System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                    System.exit(0);
                }
            }
        }
        sc.close();
		return true;
    };

    @Override
    public void run() {
        synchronized (this) {
            try {
            	cardPayment.payment(amount);
            }
            catch (Exception ime) {
                System.out.println("\n\n*** Transaction Failed .Try Again Later ***");
            }
        }
    }
}
