package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.SavingsAccount;
import models.User;

public class UsersDAO extends DatabaseConnectionFactory {

	public static boolean createNewUser(User newUser) {
		String uuid = newUser.getUserId().toString();
		String email = newUser.getEmail();
		String password = newUser.getPassword();
		Long phone = newUser.getPhone();
		String name = newUser.getName();

		final String CREATE_NEW_USER_QUERY = "INSERT INTO safebankdb.users "
				+ "(user_id, name, email, password, phone) " + "VALUES('" + uuid + "', '" + name + "', '" + email
				+ "', '" + password + "', '" + phone + "')";

		boolean createdUser = executeUpdate(CREATE_NEW_USER_QUERY);
		if (createdUser)
			return true;
		return false;
	}

	public static boolean updateUserPassword(String encryptedPassword) {
		final String UPDATE_USER_PASSWORD_QUERY = "";
		boolean updatedUserPassword = executeUpdate(UPDATE_USER_PASSWORD_QUERY);
		if (updatedUserPassword)
			return true;
		return false;
	}

	public static boolean updateUserPhone(Long phone) {
		final String UPDATE_USER_PHONE_QUERY = "";
		boolean updatedUserPhone = executeUpdate(UPDATE_USER_PHONE_QUERY);
		if (updatedUserPhone)
			return true;
		return false;
	}

	public static boolean updateUserName(String name) {
		final String UPDATE_USER_NAME = "";
		boolean updatedUserName = executeUpdate(UPDATE_USER_NAME);
		if (updatedUserName)
			return true;
		return false;
	}

	public static User getUserByEmail(String email) {
		return getUserData(email);
//		ResultSet users = null;
//		List<User> retrievedUser = null;
//		try {
//			final String GET_USER_QUERY = "SELECT * "
//					+ "FROM safebankdb.users "
//					+ "WHERE email = '"+email+"';";
//
//			users = executeQuery(GET_USER_QUERY);
//			retrievedUser = new ArrayList<>();
//			User user  = new User();
//			while(users.next()) {
//				user.setName(users.getString("name"));
//				user.setEmail(users.getString("email"));
//				user.setPassword(users.getString("password"));
//				user.setPhone(users.getLong("phone"));
//				user.setUserId(UUID.fromString(users.getString("user_id")));
//				user.setCreatedAt(users.getTimestamp("created_at"));
//				user.setUpdatedAt(users.getTimestamp("updated_at"));
//				retrievedUser.add(user);
//			}
//			users.close();
//		} catch (SQLException sqlException) {
//			// TODO Auto-generated catch block
//			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
//		}
//		finally {
//			closeConnection();
//		}
//		return !retrievedUser.isEmpty() ? retrievedUser.get(0) : null;
	}

	public static User getUserById(String userId) {
		User getUserById = getUserData(userId);
		return getUserById;
//		ResultSet users = null;
//		List<User> retrievedUser = null;
//		try {
//			final String GET_USER_QUERY = "SELECT * "
//					+ "FROM safebankdb.users "
//					+ "WHERE user_id = '"+userId+"';";
//
//			users = executeQuery(GET_USER_QUERY);
//			retrievedUser = new ArrayList<>();
//			User user  = new User();
//			while(users.next()) {
//				user.setUserId(UUID.fromString(users.getString("user_id")));
//				user.setName(users.getString("name"));
//				user.setEmail(users.getString("email"));
//				user.setPassword(users.getString("password"));
//				user.setPhone(users.getLong("phone"));
//				user.setCreatedAt(users.getTimestamp("created_at"));
//				user.setUpdatedAt(users.getTimestamp("updated_at"));
//				retrievedUser.add(user);
//			}
//			users.close();
//		} catch (SQLException sqlException) {
//			// TODO Auto-generated catch block
//			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
//		}
//		finally {
//			closeConnection();
//		}
//		return !retrievedUser.isEmpty() ? retrievedUser.get(0) : null;
	}

	private static User getUserData(String entity) {
		ResultSet users = null;
		List<User> retrievedUser = null;
		try {
			final String GET_USER_QUERY = "SELECT * " + "FROM safebankdb.users " + "WHERE "
					+ (entity.contains("@") ? "email" : "user_id") + " = '" + entity + "';";

			users = executeQuery(GET_USER_QUERY);
			retrievedUser = new ArrayList<>();
			User user = new User();
			while (users.next()) {
				user.setName(users.getString("name"));
				user.setEmail(users.getString("email"));
				user.setPassword(users.getString("password"));
				user.setPhone(users.getLong("phone"));
				user.setUserId(UUID.fromString(users.getString("user_id")));
				user.setCreatedAt(users.getTimestamp("created_at"));
				user.setUpdatedAt(users.getTimestamp("updated_at"));
				user.setCreditScore(users.getInt("credit_score"));
				retrievedUser.add(user);
			}
			users.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DatabaseConnectionFactory.class.getName()).log(Level.SEVERE, null, sqlException);
		} finally {
			closeConnection();
		}
		return !retrievedUser.isEmpty() ? retrievedUser.get(0) : null;
	}

	public static boolean updateUserCreditScore(String userId, int creditScore) {

		final String UPDATE_USER_CREDIT_SCORE_QUERY = "UPDATE users " + "SET credit_score = '" + creditScore + "'"
				+ " WHERE user_id = '" + userId + "'";

		boolean updatedRemainingCreditLimit = executeUpdate(UPDATE_USER_CREDIT_SCORE_QUERY);
		if (updatedRemainingCreditLimit)
			return true;
		return false;
	}

	public static boolean userExists(String email) {
		User user = getUserByEmail(email);
		if (user != null)
			return true;
		return false;
	}
}
