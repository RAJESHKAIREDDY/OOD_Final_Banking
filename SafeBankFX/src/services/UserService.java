package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dao.BeneficiaryUsersDAO;
import dao.CreditCardsDAO;
import dao.SavingsAccountsDAO;
import dao.TransactionsDAO;
import dao.UserDAO;
import models.BeneficiaryUser;
import models.CreditCard;
import models.SavingsAccount;
import models.Transaction;
import models.User;

public class UserService {
	
	public static User createNewUser(Map<String, Object> newUserData) {
		
		//textfield name - rajesh
		//textfield age - 23
		//textfield phone - 8689797897
		
		User newUser = new User();
		newUser.setUserId(UUID.randomUUID());
		newUser.setName((String) newUserData.get("name"));
		newUser.setEmail((String) newUserData.get("email"));
		newUser.setPassword((String) newUserData.get("password"));
		newUser.setPhone((long) Long.parseLong((String) newUserData.get("phone")));
		newUser.setAccounts(new ArrayList<SavingsAccount>());
		newUser.setCreditCards(new ArrayList<CreditCard>());
		newUser.setTransactions(new ArrayList<Transaction>());
		newUser.setBeneficiaryUsers(new ArrayList<BeneficiaryUser>());
		
		boolean existingUser = UserDAO.userExists(newUser.getEmail());
		User user = null;
		if(!existingUser) {
			boolean created = UserDAO.createNewUser(newUser);
			if(created) {
				user = UserDAO.getUser(newUser.getEmail());
				System.out.println("Created Account for User "+newUser.getName());
				return user;
			}
			else {
				System.out.println("User Already Exists / Some Error Occurred");
				return null;
			}
		}
		else {
			user = UserDAO.getUser(newUser.getEmail());
			String userId = user.getUserId().toString();
			List<SavingsAccount> accounts = 
					SavingsAccountsDAO.getUserSavingsAccounts(userId);
			List<CreditCard> cards = 
					CreditCardsDAO.getUserCreditCards(userId);
			List<Transaction> transactions = 
					TransactionsDAO.getUserTransactions(userId);
			List<BeneficiaryUser> beneficiaries = 
					BeneficiaryUsersDAO.getBeneficiaries(userId);
			
			user.setAccounts(accounts);
			user.setBeneficiaryUsers(beneficiaries);
			user.setCreditCards(cards);
			user.setTransactions(transactions);

			return user;
		}
	}
}
