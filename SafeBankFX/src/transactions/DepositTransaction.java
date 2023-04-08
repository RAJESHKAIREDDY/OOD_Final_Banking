package transactions;

import validations.ValidatePin;
import java.util.Scanner;
import java.util.UUID;

import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.Transaction;

public class DepositTransaction extends Thread {

    double amount;
    double accountBalance;
    String userId;
    String accountId;
    public DepositTransaction(String userId, String accountId, double accountBalance, double amount) {
        this.amount = amount;
        this.userId = userId;
        this.accountId = accountId;
        this.accountBalance = accountBalance;
    }

    private final Deposit deposit = (double amount) -> {
   
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
                    accountBalance += amount;
                    System.out.println("\n\n*** Deposited USD " + amount + " to Savings A/C Account ID : "+accountId+" ***");
                    System.out.println("Updated Balance : USD "+accountBalance);
                    SavingsAccountsDAO.updateAccountBalance(userId, accountId, accountBalance);
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(UUID.randomUUID());
                    transaction.setTransactionCategory(TransactionCategory.CASH_DEPOSIT);
                    transaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
                    transaction.setTransactionMode(TransactionMode.CREDIT);
                    transaction.setTransactionName("Deposit for Medical Purpose");
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
                deposit.deposit(amount);
            }
            catch (Exception ime) {
                System.out.println("\n\n*** Transaction Failed .Try Again Later ***");
            }
        }
    }
}
