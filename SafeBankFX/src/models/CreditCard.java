package models;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class CreditCard extends Card {
    private static final AtomicInteger count = new AtomicInteger(0);
    private final int id = count.incrementAndGet();
    private String nameOnCard;
    private CardType cardType;
    private long cardNumber;
    private int securityCode;
    private int pinNumber;
    private CardCategory cardCategory;
    private double creditLimit;
    private double remainingCreditBalance;
    private Date lastDueDate;
    private Date lastPaymentDate;
    private Date createdAt;
    private Date validThru;

    public Date getValidThru() {
        return validThru;
    }

    public void setValidThru(Date validThru) {
        this.validThru = validThru;
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

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    public double getRemainingCreditBalance() {
        return remainingCreditBalance;
    }

    public void setRemainingCreditBalance(double remainingCreditBalance) {
        this.remainingCreditBalance = remainingCreditBalance;
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public Date getLastDueDate() {
        return lastDueDate;
    }

    public void setLastDueDate(Date lastDueDate) {
        this.lastDueDate = lastDueDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", nameOnCard='" + nameOnCard + '\'' +
                ", cardType=" + cardType +
                ", cardNumber=" + cardNumber +
                ", securityCode=" + securityCode +
                ", pinNumber=" + pinNumber +
                ", cardCategory=" + cardCategory +
                ", creditLimit=" + creditLimit +
                ", remainingCreditBalance=" + remainingCreditBalance +
                ", lastDueDate=" + lastDueDate +
                ", lastPaymentDate=" + lastPaymentDate +
                ", createdAt=" + createdAt +
                ", validThru=" + validThru +
                '}';
    }
}
