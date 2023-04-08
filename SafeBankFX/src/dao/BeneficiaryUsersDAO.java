package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.BeneficiaryUser;

public class BeneficiaryUsersDAO extends DBInstance {

	public static boolean createNewBeneficiary(String userId, BeneficiaryUser beneficiaryUser) {
		
		String beneficiaryId = beneficiaryUser.getBeneficiaryUserId().toString();
		
		boolean beneficiaryUserExists = beneficiaryUserExists(beneficiaryId);
		
		if(beneficiaryUserExists)
			return false;
		
		final String CREATE_NEW_BENEFICIARY_QUERY = "INSERT INTO "
				+ "beneficiary_users("
				+ "beneficiary_user_id, "
				+ "user_id) "
				
				+ "VALUES ("
				+ "'" + beneficiaryId + "', "
				+ "'" + userId + "')";
		
		boolean createdCreditCard = executeUpdate(CREATE_NEW_BENEFICIARY_QUERY);
		if(createdCreditCard)
			return true;
		return false;
	}
	
	public static boolean deleteBeneficiary(String beneficiaryId) {
		
		final String DELETE_BENEFICIARY_QUERY = "DELETE FROM "
				+ "beneficiary_users"
				+ "WHERE"
				+ "beneficiary_user_id = '" + beneficiaryId + "'";
		
		boolean createdCreditCard = executeUpdate(DELETE_BENEFICIARY_QUERY);
		if(createdCreditCard)
			return true;
		return false;
	}
	
	public static BeneficiaryUser getBeneficiary(String beneficiaryUserId) {
		ResultSet beneficiaries = null;
		BeneficiaryUser beneficiaryUser = null;
		try {
			final String GET_BENEFICIARY_USER_QUERY = "SELECT * "
					+ "FROM safebankdb.beneficiary_users "
					+ "WHERE beneficiary_user_id = '"+beneficiaryUserId+"'";

			beneficiaries = executeQuery(GET_BENEFICIARY_USER_QUERY);
			beneficiaryUser  = new BeneficiaryUser();
			while(beneficiaries.next()) {
				beneficiaryUser.setId(beneficiaries.getInt("id"));
				beneficiaryUser.setBeneficiaryUserId(UUID.fromString(beneficiaries.getString("beneficiary_user_id")));
				beneficiaryUser.setCreatedAt(beneficiaries.getTimestamp("created_at"));
			}
			beneficiaries.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		finally {
			closeConnection();
		}
		return beneficiaryUser;
	}
	
	public static List<BeneficiaryUser> getBeneficiaries(String userId) {
		ResultSet results = null;
		List<BeneficiaryUser> retrievedBeneficiaries = null;
		try {
			final String GET_ACCOUNTS_QUERY = "SELECT * "
					+ "FROM safebankdb.beneficiary_users "
					+ "WHERE user_id = '"+userId+"';";

			results = (ResultSet) executeQuery(GET_ACCOUNTS_QUERY);
			retrievedBeneficiaries = new ArrayList<>();
			BeneficiaryUser beneficiaryUser = new BeneficiaryUser();
			while(results.next()) {
				beneficiaryUser.setId(results.getInt("id"));
				beneficiaryUser.setBeneficiaryUserId(UUID.fromString(results.getString("beneficiary_user_id")));
				beneficiaryUser.setCreatedAt(results.getTimestamp("created_at"));
				retrievedBeneficiaries.add(beneficiaryUser);
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return retrievedBeneficiaries;
	}
	
	public static boolean beneficiaryUserExists(String beneficiaryId) {
		BeneficiaryUser beneficiaryUser = getBeneficiary(beneficiaryId);
		if(beneficiaryUser != null)
			return true;
		return false;
	}
}
