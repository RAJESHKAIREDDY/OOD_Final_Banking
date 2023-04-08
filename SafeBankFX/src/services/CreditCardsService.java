package services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import dao.CreditCardsDAO;
import enums.CardCategory;
import enums.CardProvider;
import models.CreditCard;
import models.User;
import utils.CreditCardUtils;

public class CreditCardsService {
	
	public static boolean createNewCreditCard(
			String userId, User user) throws Exception {
		CreditCard creditCard = new CreditCard();
		
		CardCategory cardCategory = CreditCardUtils.getCardCategory(user);
		
		double totalCreditLimit = 
				CreditCardUtils.getTotalCreditLimit(cardCategory);
		
		long cardNumber = CreditCardUtils.generateCardNumber();
		int securityCode = CreditCardUtils.generateSecurityCode();
		
		CardProvider cardProvider = 
				CreditCardUtils.getCardProvider(cardNumber);
		
		Date validThru = CreditCardUtils.getValidThru(new Date());
		
		creditCard.setPinNumber(0000);
		
		creditCard.setCreditCardId(UUID.randomUUID());
		creditCard.setCardNumber(cardNumber);
		creditCard.setSecurityCode(securityCode);
		creditCard.setTotalCreditLimit(totalCreditLimit);
		creditCard.setRemainingCreditLimit(totalCreditLimit);
		creditCard.setCardCategory(cardCategory);
		creditCard.setCardProvider(cardProvider);
		creditCard.setValidThru(validThru);
		
		boolean cardCreated = 
				CreditCardsDAO.createNewCreditCard(userId, creditCard);
		
		if(cardCreated) {
			System.out.println("Created Credit Card for User "+user.getName());
			List<CreditCard> userCards = 
					CreditCardsDAO.getUserCreditCards(userId);
			user.setCreditCards(userCards);
			return true;
		}
		else {
			System.out.println("Failed Creating Credit Card for "+user.getName());
			return false;
		}
	}
}
