package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import enums.CardCategory;
import enums.CardProvider;
import models.CreditCard;

public class CreditCardsDAO extends DBInstance {
	
	public static boolean createNewCreditCard(String userId, CreditCard creditCard) {
		
		String creditCardId = creditCard.getCreditCardId().toString();
		Long cardNumber = creditCard.getCardNumber();
		int securityCode = creditCard.getSecurityCode();
		int pinNumber = creditCard.getPinNumber();
		double totalCreditLimit = creditCard.getTotalCreditLimit();
		double remainingCreditLimit = creditCard.getRemainingCreditLimit();
		Timestamp validThru = new Timestamp(creditCard.getValidThru().getTime());
		CardProvider cardProvider = creditCard.getCardProvider();
		CardCategory cardCategory = creditCard.getCardCategory();
		
		
		final String CREATE_NEW_CREDIT_CARD_QUERY = "INSERT INTO "
				+ "`safebankdb`.`credit_cards`("
				+ "credit_card_id,"
				+ "card_number,"
				+ "security_code,"
				+ "pin_number,"
				+ "total_credit_limit,"
				+ "remaining_credit_limit,"
				+ "card_category,"
				+ "card_provider,"
				+ "user_id,"
				+ "valid_thru) "

				+ "VALUES ('" + creditCardId + "',"
				+ cardNumber + ","
				+ securityCode + ","
				+ pinNumber + ","
				+ totalCreditLimit + ","
				+ remainingCreditLimit + ","
				+ "'" + cardCategory.name() + "',"
				+ "'" + cardProvider.name() + "',"
				+ "'" + userId + "',"
				+ "'" + validThru + "')";
	

		boolean createdCreditCard = executeUpdate(CREATE_NEW_CREDIT_CARD_QUERY);
		if(createdCreditCard)
			return true;
		return false;
	}
	
	public static List<CreditCard> getUserCreditCards(String userId) {
		ResultSet results = null;
		List<CreditCard> retrievedCards = null;
		try {
			final String GET_CARDS_QUERY = "SELECT * "
					+ "FROM safebankdb.credit_cards "
					+ "WHERE user_id = '"+userId+"';";

			results = (ResultSet) executeQuery(GET_CARDS_QUERY);
			retrievedCards = new ArrayList<>();
			CreditCard creditCard = new CreditCard();
			while(results.next()) {				
				creditCard.setId(results.getInt("id"));
				creditCard.setCreditCardId(UUID.fromString(results.getString("credit_card_id")));
				creditCard.setCardNumber(results.getLong("card_number"));
				creditCard.setSecurityCode(results.getInt("security_code"));
				creditCard.setPinNumber(results.getInt("pin_number"));
				creditCard.setTotalCreditLimit(results.getDouble("total_credit_limit"));
				creditCard.setRemainingCreditLimit(results.getDouble("remaining_credit_limit"));
				creditCard.setCardCategory(CardCategory.valueOf((String) results.getObject("card_category")));
				creditCard.setCardProvider(CardProvider.valueOf((String) results.getObject("card_provider")));
				creditCard.setValidThru(results.getTimestamp("valid_thru"));
				creditCard.setLastDueDate(results.getTimestamp("last_due_date"));
				creditCard.setLastPaymentDate(results.getTimestamp("last_payment_date"));
				creditCard.setCreatedAt(results.getTimestamp("created_at"));
				creditCard.setUpdatedAt(results.getTimestamp("updated_at"));
				retrievedCards.add(creditCard);
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return retrievedCards;
	}
	
	public static CreditCard getCreditCard(String creditCardId) {
		ResultSet results = null;
		CreditCard creditCard = null;
		try {
			final String GET_CARDS_QUERY = "SELECT * "
					+ "FROM safebankdb.credit_cards "
					+ "WHERE credit_card_id = '"+creditCardId+"';";

			results = (ResultSet) executeQuery(GET_CARDS_QUERY);
			creditCard = new CreditCard();
			while(results.next()) {				
				creditCard.setId(results.getInt("id"));
				creditCard.setCreditCardId(UUID.fromString(results.getString("credit_card_id")));
				creditCard.setCardNumber(results.getLong("card_number"));
				creditCard.setSecurityCode(results.getInt("security_code"));
				creditCard.setPinNumber(results.getInt("pin_number"));
				creditCard.setTotalCreditLimit(results.getDouble("total_credit_limit"));
				creditCard.setRemainingCreditLimit(results.getDouble("remaining_credit_limit"));
				creditCard.setCardCategory(CardCategory.valueOf((String) results.getObject("card_category")));
				creditCard.setCardProvider(CardProvider.valueOf((String) results.getObject("card_provider")));
				creditCard.setValidThru(results.getTimestamp("valid_thru"));
				creditCard.setLastDueDate(results.getTimestamp("last_due_date"));
				creditCard.setLastPaymentDate(results.getTimestamp("last_payment_date"));
				creditCard.setCreatedAt(results.getTimestamp("created_at"));
				creditCard.setUpdatedAt(results.getTimestamp("updated_at"));
			}
		} catch (SQLException sqlException) {
			// TODO Auto-generated catch block
			Logger.getLogger(DBInstance.class.getName()).log(Level.SEVERE, null, sqlException);
		}
		return creditCard;
	}
	
	public static boolean updateRemainingCreditLimit(String userId, String cardId, double remainingCreditLimit) {
		
		final String UPDATE_REMAINING_CREDIT_LIMIT_QUERY = "UPDATE credit_cards "
				+ "SET remaining_credit_limit = "
				+ "'" + remainingCreditLimit + "'"
				+ " WHERE user_id = "
				+ "'" + userId + "'"
				+ " AND credit_card_id = "
				+ "'" + cardId + "'";
				
		boolean updatedRemainingCreditLimit = executeUpdate(UPDATE_REMAINING_CREDIT_LIMIT_QUERY);
		if(updatedRemainingCreditLimit)
			return true;
		return false;
	}
	
	public static boolean creditCardExists(String creditCardId) {
		CreditCard creditCard = getCreditCard(creditCardId);
		if(creditCard != null)
			return true;
		return false;
	}
}
