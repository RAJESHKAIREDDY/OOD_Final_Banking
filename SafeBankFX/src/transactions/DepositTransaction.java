package transactions;

import models.*;
import banking.BankingServices;
import validations.ValidatePin;

import java.io.Console;
import java.util.Scanner;

public class DepositTransaction extends Thread {

    double amount;
    Customer customer;
    public DepositTransaction(Customer customer, double amount) {
        this.amount = amount;
        this.customer = customer;
    }

    private final Deposit deposit = (double amount) -> {
        double accountBalance = customer.getDebitCard().getAccountBalance();
        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        boolean iterate = true;
        int iterateCountForInvalid = 0;
        int iterateCountForIncorrect = 0;
        if (console == null) {
            System.out.println("Console not available");
            System.exit(1);
        }
        while (iterate) {
            char[] pinArray = console.readPassword("n\n*** Enter your debit card pin number ***");
            String pin = new String(pinArray);
            boolean isPinValid = ValidatePin.isPinValid(pin);
            if(isPinValid) {
                int pinNumber = Integer.parseInt(pin);
                if(pinNumber == customer.getDebitCard().getPinNumber()) {
                    accountBalance = accountBalance + amount;
                    customer.getDebitCard().setAccountBalance(accountBalance);
                    System.out.println("\n\n*** Deposited USD " + amount + " to Savings A/C : "+customer.getDebitCard().getAccountNumber()+ " ***");
                    System.out.println("Updated Balance : USD "+customer.getDebitCard().getAccountBalance());
                    //record transaction
                    Transaction transaction = new Transaction();
                    transaction.setTransactionType(TransactionType.DEPOSIT);
                    transaction.setTransactionMode(TransactionMode.CREDIT);
                    transaction.setAmount(amount);
                    transaction.setCardType(CardType.DEBIT_CARD);
                    customer.getTransactionList().getTransactions().add(transaction);
                    BankingServices.askForContinuation();
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
    };

    @Override
    public void run() {
        synchronized (this) {
            try {
                deposit.processDeposit(amount);
            }
            catch (Exception ime) {
                System.out.println("\n\n*** Transaction Failed .Try Again Later ***");
            }
        }
    }
}
