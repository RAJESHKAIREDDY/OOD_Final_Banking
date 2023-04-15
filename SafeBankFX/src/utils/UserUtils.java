package utils;

import dao.UsersDAO;
import javafx.scene.control.TextField;
import models.User;

public class UserUtils {

	public static boolean isValidEmail(TextField textField) {
		return true;
	}

	public static boolean isPasswordValid(TextField textField) {
		return true;
	}

	public static boolean isEmailPasswordValid(String email, String password) {
		boolean userExists = UsersDAO.userExists(email);
		if(!userExists) {
			return false;
		}
		User user = UsersDAO.getUserByEmail(email);
		String hashedPasswordFromDB = user.getPassword();
		boolean passwordHashMached = 
				PasswordUtils.checkPassword(password, hashedPasswordFromDB);
		if(passwordHashMached)
			return true;
		return false;
	}
}
