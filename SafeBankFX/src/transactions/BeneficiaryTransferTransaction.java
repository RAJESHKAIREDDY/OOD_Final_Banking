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
import models.SavingsAccount;
import models.Transaction;
import models.User;

public class BeneficiaryTransferTransaction extends Thread {
	
	private volatile boolean running = true;
	
	private String senderUserId;
	private String receiverUserId;
	private String senderAccountId;
	private String receiverAccountId;
	private double amount;
	
	public void stopRunning() {
        running = false;
    }
	
	public BeneficiaryTransferTransaction(
			String senderUserId, 
			String receiverUserId, 
			String senderAccountId,
			String receiverAccountId, 
			double amount) {
		super();
		this.senderUserId = senderUserId;
		this.receiverUserId = receiverUserId;
		this.senderAccountId = senderAccountId;
		this.receiverAccountId = receiverAccountId;
		this.amount = amount;
	}

	public void transferToBeneficiary() throws Exception {
        
		SavingsAccount senderAccount = SavingsAccountsDAO.getSavingsAccountById(senderAccountId);
		SavingsAccountsDAO.processTransfer(senderAccountId, receiverAccountId, amount);
        Transaction senderTransaction = new Transaction();
        senderTransaction.setTransactionId(UUID.randomUUID());
        senderTransaction.setTransactionCategory(TransactionCategory.TRANSFER_TO_BENEFICIARY);
        senderTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
        senderTransaction.setTransactionMode(TransactionMode.DEBIT);
        senderTransaction.setTransactionName("Transfer to Beneficiary");
        senderTransaction.setAmount(amount);
        senderTransaction.setAccountNumber(senderAccount.getAccountNumber());
      	TransactionsDAO.createNewTransaction(senderUserId, senderTransaction);
      	
      	User sender = UsersDAO.getUserById(senderUserId);
      	int creditScore = sender.getCreditScore();
		sender.setCreditScore(creditScore + 10);
		
		UsersDAO.updateUserCreditScore(senderUserId, sender.getCreditScore());
      	
		SavingsAccount recieverAccount = SavingsAccountsDAO.getSavingsAccountById(receiverAccountId);
      	Transaction receiverTransaction = new Transaction();
        senderTransaction.setTransactionId(UUID.randomUUID());
        senderTransaction.setTransactionCategory(TransactionCategory.TRANSFER_FROM_BENEFICIARY);
        senderTransaction.setTransactionType(TransactionType.ACCOUNT_TRANSACTION);
        senderTransaction.setTransactionMode(TransactionMode.CREDIT);
        senderTransaction.setTransactionName("Transfer from Beneficiary");
        senderTransaction.setAmount(amount);
        receiverTransaction.setAccountNumber(recieverAccount.getAccountNumber());
      	TransactionsDAO.createNewTransaction(receiverUserId, receiverTransaction);
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
			synchronized (this) {
				try {
					transferToBeneficiary();
				} catch (Exception exception) {
					// TODO Auto-generated catch block
					Logger
					.getLogger(BeneficiaryTransferTransaction.class.getName())
	    			.log(Level.SEVERE, null, exception);
				}
			}
	}
}
