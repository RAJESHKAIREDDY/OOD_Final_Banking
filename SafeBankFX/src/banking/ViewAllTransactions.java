package banking;

import models.Customer;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewAllTransactions {
    public static void viewAllTransactions(Customer customer) throws Exception {
        System.out.println("\n************************************************ Starting "+customer.getName()+"'s Transactions ************************************************\n");
        System.out.println("S.no\t\tAmount\t\t\tTransaction Mode\t\tCreated At\t\t\t\t\t\t\tCard Type\t\tTransaction ID\n");
        AtomicInteger count = new AtomicInteger();
        customer.getTransactionList().getTransactions().forEach(transaction -> {
            System.out.println((count.getAndIncrement() + 1)+"\t\t\tUSD "+transaction.getAmount()+
                    "\t\t"+transaction.getTransactionMode()+"\t\t\t\t\t"+transaction.getTransactionDate()+
                    "\t\t"+transaction.getCardType()+"\t\t"+transaction.getTransactionId()+"\n");
        });
        System.out.println("\n************************************************* Ending "+customer.getName()+"'s Transactions *************************************************\n");
        BankingServices.askForContinuation();
    }

    public static void checkBalance(Customer customer) throws Exception {
        System.out.println("\n******************** "+customer.getName()+"'s Account Balance Details ********************");
        System.out.println("\nSavings Account Balance  : "+customer.getDebitCard().getAccountBalance());
        System.out.println("\nCredit Remaining Balance : "+customer.getCreditCard().getRemainingCreditBalance());
        System.out.println("\nTotal Credit Limit       : "+customer.getCreditCard().getCreditLimit());
        System.out.println("\n******************** "+customer.getName()+"'s Account Balance Details ********************");
        BankingServices.askForContinuation();
    }
}
