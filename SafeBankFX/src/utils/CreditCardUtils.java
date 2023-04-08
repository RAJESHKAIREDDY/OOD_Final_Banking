package utils;

import java.util.Calendar;
import java.util.Date;
import enums.CardCategory;
import enums.CardProvider;
import models.User;

public class CreditCardUtils {
	
	public static double getTotalCreditLimit(CardCategory cardCategory) {
		double totalCreditLimit = 0;
		if(cardCategory.equals(CardCategory.BURGUNDY))
			totalCreditLimit = 10000;
		else if(cardCategory.equals(CardCategory.GOLD))
			totalCreditLimit = 20000;
		else if(cardCategory.equals(CardCategory.PLATINUM))
			totalCreditLimit = 30000;
		return totalCreditLimit;
	}
	
	public static long generateCardNumber() {
        long min = 1000000000000000L; // minimum 16 digit number
        long max = 9999999999999999L; // maximum 16 digit number
        long range = max - min + 1;
        long randomNum = (long) (Math.random() * range) + min;
        String randNumStr = randomNum + "";
        int[] cardPrefixes = {4,5,6,34,37};
        if(!randNumStr.startsWith("4") ||
                !randNumStr.startsWith("5") ||
                !randNumStr.startsWith("6") ||
                !randNumStr.startsWith("34")||
                !randNumStr.startsWith("37")) {
            int prefix = cardPrefixes[(int) Math.ceil(Math.random() * 4)];

            if(prefix == 34 || prefix == 37) {
                randNumStr = prefix + randNumStr.substring(2);
            }
            else {
                randNumStr = prefix + randNumStr.substring(1);
            }
            return Long.parseLong(randNumStr);
        }
        else {
            return randomNum;
        }
    }
	
	public static int generateSecurityCode() {
        int min = 100; // minimum 3 digit number
        int max = 999; // maximum 3 digit number
        return (int) (Math.random() * (max - min + 1)) + min;
    }
	
    public static CardProvider getCardProvider(double cardNumber) {
    	String cardNumberString = cardNumber + "";
        if(cardNumberString.startsWith("4"))
            return CardProvider.VISA;
        else if(cardNumberString.startsWith("5"))
            return CardProvider.MASTER_CARD;
        else if(cardNumberString.startsWith("6"))
            return CardProvider.AMERICAN_EXPRESS;
        return CardProvider.DISCOVER;
    }
    
    public static Date getValidThru(Date inputDate) throws Exception {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.YEAR, 5);
    	return calendar.getTime();
    }
    
    public static Date createPaymentDueDate(Date date, int days) throws Exception {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);     
    	return calendar.getTime();
    }
    
    public static CardCategory getCardCategory(User user) {
    	int creditScore = user.getCreditScore();
    	boolean burgundyCardRange = creditScore >= 820 && creditScore <= 839;
    	boolean goldCardRange = creditScore >= 840 && creditScore <= 859;
    	boolean platinumCardRange = creditScore >= 860;
    	
    	if(user.getCreditCards() == null || burgundyCardRange)
    		return CardCategory.BURGUNDY;
    	else if(goldCardRange)
    		return CardCategory.GOLD;
    	else if(platinumCardRange)
    		return CardCategory.PLATINUM;
    	return null;
    }
}
