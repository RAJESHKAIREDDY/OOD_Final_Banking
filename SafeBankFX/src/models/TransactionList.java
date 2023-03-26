package models;

import java.util.List;

public class TransactionList {

    List<Transaction> transactions;

    public TransactionList(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
