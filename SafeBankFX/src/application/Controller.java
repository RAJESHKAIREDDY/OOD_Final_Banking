package application;

import dao.UsersDAO;
import models.User;

public class Controller {
	protected static User user;
	protected static boolean isSessionActive;
	protected static void refreshState() {
		user = UsersDAO.getUserById(user.getUserId().toString());
	}
}
