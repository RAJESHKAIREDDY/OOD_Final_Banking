package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.SavingsAccount;

public class SavingsAccountsDAO extends DBInstance {
	
	public static boolean createNewAccount(String userId, SavingsAccount savingsAccount) {
		
		String accountId = savingsAccount.getAccountId().toString();
		long accountNumber = savingsAccount.getAccountNumber();
		double accountBalance = savingsAccount.getAccountBalance();
	
		final String CREATE_NEW_SAVINGS_ACCOUNT_QUERY = "INSERT INTO "
				+ "savings_accounts("
				+ "account_id, "
				+ "account_number, "
				+ "account_balance, "
				+ "user_id) "
				
				+ "VALUES ("
				+ "'" + accountId + "',"
				+ "'" + accountNumber + "',"
				+ "'" + accountBalance + "', "
				+ "'" + userId + "')";

		boolean createdSavingsAccount = executeUpdate(CREATE_NEW_SAVINGS_ACCOUNT_QUERY);
		if(createdSavingsAccount)
			return true;
		return false;
	}
	
	public static List<SavingsAccount> getUserSavingsAccounts(String userId) {
		ResultSet results = null;
		List<SavingsAccount> retrievedAccounts = null;
		try {
			final String GET_ACCOUNTS_QUERY = "SELECT * "
					+ "FROM safebankdb.savings_accounts "
					+ "WHERE user_id = '"+userId+"';";

			results = (ResultSet) executeQuery(GET_ACCOUNTS_QUERY);
			retrievedAccounts = new ArrayList<>();
			SavingsAccount savingsAccount = new SavingsAccount();
			while(results.next()) {
				savingsAccount.setId(results.getInt("id"));
				savingsAccount.setAccountId(UUID.fromString(results.getString("account_id")));
				savingsAccount.setAccountNumber(results.getLong("account_number"));
				savingsAccount.setAccountBalance(results.getDouble("account_balance"));
				savingsAccount.setCreatedAt(results.getTimestamp("created_at"));
				savingsAccount.setUpdatedAt(results.getTimestamp("updated_at"));
				retrievedAccounts.add(savingsAccount);
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return retrievedAccounts;
	}
	
	public static SavingsAccount getUserSavingsAccount(String accountId) {
		ResultSet results = null;
		SavingsAccount account = null;
		try {
			final String GET_ACCOUNTS_QUERY = "SELECT * "
					+ "FROM safebankdb.savings_accounts "
					+ "WHERE account_id = '"+accountId+"';";

			results = (ResultSet) executeQuery(GET_ACCOUNTS_QUERY);
			account = new SavingsAccount();
			while(results.next()) {
				account.setId(results.getInt("id"));
				account.setAccountId(UUID.fromString(results.getString("account_id")));
				account.setAccountNumber(results.getLong("account_number"));
				account.setAccountBalance(results.getDouble("account_balance"));
				account.setCreatedAt(results.getTimestamp("created_at"));
				account.setUpdatedAt(results.getTimestamp("updated_at"));
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return account;
	}
	
	public static boolean updateAccountBalance(String userId, String accountId, double updatedBalance) {
	
		final String UPDATE_ACCOUNT_BALANCE_QUERY = "UPDATE savings_accounts "
				+ "SET account_balance = "
				+ "'" + updatedBalance + "'"
				+ " WHERE user_id = "
				+ "'" + userId + "'"
				+ " AND account_id = "
				+ "'" + accountId + "'";
				
		boolean createdSavingsAccount = executeUpdate(UPDATE_ACCOUNT_BALANCE_QUERY);
		if(createdSavingsAccount)
			return true;
		return false;
	}
	
	public static boolean accountExists(String accountId) {
		SavingsAccount account = getUserSavingsAccount(accountId);
		if(account != null)
			return true;
		return false;
	}
}
