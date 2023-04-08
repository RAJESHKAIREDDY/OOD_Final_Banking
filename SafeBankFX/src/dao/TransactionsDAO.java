package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import enums.TransactionCategory;
import enums.TransactionMode;
import enums.TransactionType;
import models.Transaction;

public class TransactionsDAO extends DBInstance {
	
	public static boolean createNewTransaction(String userId, Transaction transaction) {
		
		String transactionId = transaction.getTransactionId().toString();
		String transactionName = transaction.getTransactionName();
		String transactionCategory = transaction.getTransactionCategory().name();
		String transactionType = transaction.getTransactionType().name();
		String transactionMode = transaction.getTransactionMode().name();
		
		final String CREATE_NEW_TRANSACTION_QUERY = "INSERT INTO "
				+ "transactions("
				+ "transaction_id, "
				+ "transaction_name, "
				+ "transaction_category, "
				+ "transaction_type, "
				+ "transaction_mode, "
				+ "user_id) "
				
				+ "VALUES ("
				+ "'" + transactionId + "',"
				+ "'" + transactionName + "',"
				+ "'" + transactionCategory + "', "
				+ "'" + transactionType + "', "
				+ "'" + transactionMode + "', "
				+ "'" + userId + "')";
		
		boolean createdTransaction = executeUpdate(CREATE_NEW_TRANSACTION_QUERY);
		if(createdTransaction)
			return true;
		return false;
	}
	
	public static List<Transaction> getUserTransactions(String userId) {
		ResultSet results = null;
		List<Transaction> retrievedTransactions = null;
		try {
			final String GET_TRANSACTIONS_QUERY = "SELECT * "
					+ "FROM safebankdb.transactions "
					+ "WHERE user_id = '"+userId+"';";

			results = (ResultSet) executeQuery(GET_TRANSACTIONS_QUERY);
			retrievedTransactions = new ArrayList<>();
			Transaction transaction = new Transaction();
			while(results.next()) {
				transaction.setId(results.getInt("id"));
				transaction.setTransactionId(UUID.fromString(results.getString("transaction_id")));
				transaction.setTransactionName(results.getString("transaction_name"));
				transaction.setTransactionCategory(TransactionCategory.valueOf(results.getString("transaction_category")));
				transaction.setTransactionType(TransactionType.valueOf(results.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(results.getString("transaction_mode")));
				transaction.setCreatedAt(results.getTimestamp("created_at"));
				retrievedTransactions.add(transaction);
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return retrievedTransactions;
	}
	
	public static Transaction getTransaction(String transactionId) {
		ResultSet transactions = null;
		Transaction transaction = null;
		try {
			final String GET_TRANSACTION_QUERY = "SELECT * "
					+ "FROM safebankdb.transactions "
					+ "WHERE transaction_id = '"+transactionId+"';";

			transactions = executeQuery(GET_TRANSACTION_QUERY);
			transaction = new Transaction();
			while(transactions.next()) {
				transaction.setId(transactions.getInt("id"));
				transaction.setTransactionId(UUID.fromString(transactions.getString("transaction_id")));
				transaction.setTransactionName(transactions.getString("transaction_name"));
				transaction.setTransactionType(TransactionType.valueOf(transactions.getString("transaction_type")));
				transaction.setTransactionMode(TransactionMode.valueOf(transactions.getString("transaction_mode")));
				transaction.setTransactionCategory(TransactionCategory.valueOf(transactions.getString("transaction_category")));
				transaction.setCreatedAt(transactions.getTimestamp("created_at"));
			}
			transactions.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		finally {
			closeConnection();
		}
		return transaction;
	}
	
	public static boolean transactionExists(String transactionId) {
		Transaction transaction = getTransaction(transactionId);
		if(transaction != null)
			return true;
		return false;
	}
}
