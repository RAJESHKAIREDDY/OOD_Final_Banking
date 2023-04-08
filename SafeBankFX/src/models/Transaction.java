package models;

import java.util.Date;
import java.util.UUID;

import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;

public class Transaction {
	private int id;
	private UUID transactionId;
	private String transactionName;
	private TransactionCategory transactionCategory;
	private TransactionType transactionType;
	private TransactionMode transactionMode;
	private Date createdAt;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UUID getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public TransactionCategory getTransactionCategory() {
		return transactionCategory;
	}
	public void setTransactionCategory(TransactionCategory transactionCategory) {
		this.transactionCategory = transactionCategory;
	}
	public TransactionMode getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(TransactionMode transactionMode) {
		this.transactionMode = transactionMode;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", transactionId=" + transactionId + ", transactionName=" + transactionName
				+ ", transactionCategory=" + transactionCategory + ", transactionType=" + transactionType
				+ ", transactionMode=" + transactionMode + ", createdAt=" + createdAt + "]";
	}
}
