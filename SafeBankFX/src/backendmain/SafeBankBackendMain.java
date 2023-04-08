package backendmain;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.DBInstance;
import models.SavingsAccount;
import models.User;
import notifications.EmailService;
import notifications.MailgunEmailService;
import notifications.OTPService;
import services.BeneficiaryService;
import services.CreditCardsService;
import services.SavingsAccountService;
import services.UserService;
import transactions.DepositTransaction;
import transactions.AccountPaymentTransaction;

public class SafeBankBackendMain {
	
	public static void main(String[] args) {
		Map<String, Object> newUserData = new HashMap<>();
		newUserData.put("name", "abhi");
		newUserData.put("email", "abhi@gmail.com");
		newUserData.put("password", "abhi");
		newUserData.put("phone", "8888888888");
		
		User user = UserService.createNewUser(newUserData);
		
		String userId = user.getUserId().toString();
		SavingsAccountService.createSavingsAccount(userId, user);
		
		String toEmail = "";
		String message = "";
		String subject = "";
		
		try {
			
//		CreditCardsService.createNewCreditCard(userId, user);
//		BeneficiaryService.createBeneficaryService("sriharshaperi@gmail.com", user);
		
//		OTPService.sendSMS("+18552723703");
		EmailService.sendEmail(toEmail, subject, message);
//			MailgunEmailService.sendSimpleMessage();
		
//		System.out.println(user.toString());
//		SavingsAccount account = user.getAccounts().get(0);
//		String accountId = account.getAccountId().toString();
//		double accountBalance = account.getAccountBalance();
//		double amount = 3000;
		
//		DepositTransaction depositTransaction = 
//				new DepositTransaction(
//						userId, 
//						accountId, 
//						accountBalance, 
//						amount);
//		
//		depositTransaction.start();
		
//		OnlinePaymentTransaction onlinePaymentTransaction = 
//				new OnlinePaymentTransaction(
//						userId, 
//						accountId, 
//						accountBalance, 
//						amount);
//		
//		onlinePaymentTransaction.start();
			
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName())
			.log(Level.SEVERE, null, exception);
		}
	}
}
