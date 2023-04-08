package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.User;

public class UserDAO extends DBInstance {
	
	public static boolean createNewUser(User newUser) {
		String uuid = newUser.getUserId().toString();
		String email = newUser.getEmail();
		String password = newUser.getPassword();
		Long phone = newUser.getPhone();
		String name = newUser.getName();
		
		final String CREATE_NEW_USER_QUERY = "INSERT INTO safebankdb.users "
				+ "(user_id, name, email, password, phone) "
				+ "VALUES('"+uuid+"', '"+name+"', '"+email+"', '"+password+"', '"+phone+"')";

		boolean createdUser = executeUpdate(CREATE_NEW_USER_QUERY);
		if(createdUser)
			return true;
		return false;
	}
	
	public static boolean updateUserPassword(String encryptedPassword) {
		final String UPDATE_USER_PASSWORD_QUERY = "";
		boolean updatedUserPassword = executeUpdate(UPDATE_USER_PASSWORD_QUERY);
		if(updatedUserPassword)
			return true;
		return false;
	}
	
	public static boolean updateUserPhone(Long phone) {
		final String UPDATE_USER_PHONE_QUERY = "";
		boolean updatedUserPhone = executeUpdate(UPDATE_USER_PHONE_QUERY);
		if(updatedUserPhone)
			return true;
		return false;
	}
	
	public static boolean updateUserName(String name) {
		final String UPDATE_USER_NAME = "";
		boolean updatedUserName = executeUpdate(UPDATE_USER_NAME);
		if(updatedUserName)
			return true;
		return false;
	}
	
	public static User getUser(String email) {
		ResultSet users = null;
		List<User> retrievedUser = null;
		try {
			final String GET_USER_QUERY = "SELECT * "
					+ "FROM safebankdb.users "
					+ "WHERE email = '"+email+"';";

			users = executeQuery(GET_USER_QUERY);
			retrievedUser = new ArrayList<>();
			User user  = new User();
			while(users.next()) {
				user.setId(users.getInt("id"));
				user.setName(users.getString("name"));
				user.setEmail(users.getString("email"));
				user.setPassword(users.getString("password"));
				user.setPhone(users.getLong("phone"));
				user.setUserId(UUID.fromString(users.getString("user_id")));
				user.setCreatedAt(users.getTimestamp("created_at"));
				user.setUpdatedAt(users.getTimestamp("updated_at"));
				retrievedUser.add(user);
			}
			users.close();
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		finally {
			closeConnection();
		}
		return !retrievedUser.isEmpty() ? retrievedUser.get(0) : null;
	}
	
	public static boolean userExists(String email) {
		User user = getUser(email);
		if(user != null)
			return true;
		return false;
	}
}
