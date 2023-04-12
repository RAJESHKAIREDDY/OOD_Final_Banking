package services;

import java.util.List;

import dao.BeneficiaryUsersDAO;
import dao.UsersDAO;
import models.BeneficiaryUser;
import models.User;

public class BeneficiaryService {
	
	public static boolean addNewBeneficaryService(String beneficiaryEmail, User user) {
		String userId = user.getUserId().toString();
		BeneficiaryUser beneficiaryUser = new BeneficiaryUser();
		
		User existingUser = UsersDAO.getUserByEmail(beneficiaryEmail);
		if(existingUser != null) {
			beneficiaryUser.setBeneficiaryUserId(existingUser.getUserId());
			boolean beneficiaryCreated = 
					BeneficiaryUsersDAO.addNewBeneficiary(userId, beneficiaryUser);
			if(beneficiaryCreated) {
				System.out.println("Created Beneficiary User " +existingUser.getName()+" for User "+user.getName());
				List<BeneficiaryUser> beneficiaryUsers = BeneficiaryUsersDAO.getBeneficiaries(userId);
				user.setBeneficiaryUsers(beneficiaryUsers);
				return true;
			}
			else {
				System.out.println("Beneficiary already exists / Failed Creating Beneficiary User " +existingUser.getName()+" for "+user.getName());
				return false;
			}
		}
		else {
			System.out.println("User does not exist with "+beneficiaryEmail);
			return false;
		}
	}
}
