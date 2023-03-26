package models;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private final UUID transactionId;
    private TransactionType transactionType;
    private double amount;
    private TransactionMode transactionMode;
    private CardType cardType;
    private final Date transactionDate;

    public Transaction() {
        this.transactionId = UUID.randomUUID();
        this.transactionDate = new Date();
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(TransactionMode transactionMode) {
        this.transactionMode = transactionMode;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

}
