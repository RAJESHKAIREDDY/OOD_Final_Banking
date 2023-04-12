package transactions;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UsersDAO;
import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.Transaction;
import models.User;

public class SelfTransferTransaction extends Thread {
	
	private String userId;
	private String senderAccountId;
	private String receiverAccountId;
	private double amount;
	
	public SelfTransferTransaction(
			String userId, 
			String senderAccountId,
			String receiverAccountId, 
			double amount) {
		super();
		this.userId = userId;
		this.senderAccountId = senderAccountId;
		this.receiverAccountId = receiverAccountId;
		this.amount = amount;
	}



	public void transferToSelf() throws Exception {
        SavingsAccountsDAO.processTransfer(senderAccountId, receiverAccountId, amount);
        Transaction senderTransaction = new Transaction();
        senderTransaction.setTransactionId(UUID.randomUUID());
        senderTransaction.setTransactionCategory(TransactionCategory.TRANSFER_TO_SELF);
        senderTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
        senderTransaction.setTransactionMode(TransactionMode.DEBIT);
        senderTransaction.setTransactionName("Transfer to Self");
        senderTransaction.setAmount(amount);
      	TransactionsDAO.createNewTransaction(userId, senderTransaction);
      	
      	Transaction receiverTransaction = new Transaction();
        senderTransaction.setTransactionId(UUID.randomUUID());
        senderTransaction.setTransactionCategory(TransactionCategory.TRANSFER_FROM_SELF);
        senderTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
        senderTransaction.setTransactionMode(TransactionMode.CREDIT);
        senderTransaction.setTransactionName("Transfer from Self");
        senderTransaction.setAmount(amount);
      	TransactionsDAO.createNewTransaction(userId, receiverTransaction);
      	
      	User user = UsersDAO.getUserById(userId);
      	int creditScore = user.getCreditScore();
		user.setCreditScore(creditScore + 10);
		
		UsersDAO.updateUserCreditScore(userId, user.getCreditScore());
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (this) {
			try {
				transferToSelf();
			} catch (Exception exception) {
				// TODO Auto-generated catch block
				Logger.getLogger(SelfTransferTransaction.class.getName())
    			.log(Level.SEVERE, null, exception);
			}
		}
	}
}
