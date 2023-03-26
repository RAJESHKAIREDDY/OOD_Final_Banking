package models;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DebitCard extends Card{
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id = count.incrementAndGet();
    private String nameOnCard;
    private CardType cardType;
    private long cardNumber;
    private int pinNumber;

    public int getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    private int securityCode;
    private CardCategory cardCategory;
    private double accountBalance;
    private long accountNumber;
    private Date createdAt;
    private Date validThru;

    public Date getValidThru() {
        return validThru;
    }

    public void setValidThru(Date validThru) {
        this.validThru = validThru;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public CardCategory getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(CardCategory cardCategory) {
        this.cardCategory = cardCategory;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "DebitCard{" +
                "id=" + id +
                ", nameOnCard='" + nameOnCard + '\'' +
                ", cardType=" + cardType +
                ", cardNumber=" + cardNumber +
                ", pinNumber=" + pinNumber +
                ", securityCode=" + securityCode +
                ", cardCategory=" + cardCategory +
                ", accountBalance=" + accountBalance +
                ", accountNumber=" + accountNumber +
                ", createdAt=" + createdAt +
                ", validThru=" + validThru +
                '}';
    }
}
