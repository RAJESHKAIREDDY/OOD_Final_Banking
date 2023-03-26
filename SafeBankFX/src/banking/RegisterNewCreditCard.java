package banking;

import models.*;
import validations.ValidatePin;

import java.io.Console;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class RegisterNewCreditCard {

    public static CreditCard registerNewCreditCard(Customer customer) throws Exception {

        try {

            Console console = System.console();
            if (console == null) {
                System.out.println("Console not available");
                System.exit(1);
            }

            CreditCard creditCard = new CreditCard();
            creditCard.setCardAssociator(CardAssociator.BANK_OF_AMEERPET);
            creditCard.setCardType(CardType.CREDIT_CARD);
            long cardNumber = generateCardNumber();
            creditCard.setCardNumber(cardNumber);
            CardProvider cardProvider = getCardProvider(cardNumber + "");
            creditCard.setCardProvider(cardProvider);
            creditCard.setNameOnCard(customer.getName());
            creditCard.setCreatedAt(new Date());
            Date validThru = getValidThru(creditCard.getCreatedAt());
            creditCard.setValidThru(validThru);
            creditCard.setCardCategory(CardCategory.BURGUNDY);
            int pinNumber = setPinNumber(console);
            creditCard.setPinNumber(pinNumber);
            int securityCode = generateSecurityCode();
            creditCard.setSecurityCode(securityCode);
            creditCard.setLastDueDate(new Date());
            creditCard.setLastPaymentDate(new Date());
            int creditlimit = getCreditLimit(creditCard.getCardCategory());
            creditCard.setCreditLimit(creditlimit);
            creditCard.setRemainingCreditBalance(creditlimit);


            System.out.println("*** Generated Credit Card ***");
            return creditCard;
        }
        catch (Exception exception) {
            throw new Exception("\n\n*** Failed To Create Credit Card Details. Invalid Inputs. ***");
        }
    }
    public static long generateCardNumber() throws Exception {
        try {
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
        catch (Exception exception) {
            throw new Exception("In generateCardNumber()");
        }
    }
    public static CardProvider getCardProvider(String cardNumber) throws Exception {
        try {
            if(cardNumber.startsWith("4"))
                return CardProvider.VISA;
            else if(cardNumber.startsWith("5"))
                return CardProvider.MASTER_CARD;
            else if(cardNumber.startsWith("6"))
                return CardProvider.AMERICAN_EXPRESS;
            return CardProvider.DISCOVER;
        }
        catch (Exception exception) {
            throw new Exception("In getCardProvider()");
        }
    }
    public static Date getValidThru(Date inputDate) throws Exception {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inputDate);
            calendar.add(Calendar.YEAR, 5);
            return calendar.getTime();
        }
        catch (Exception exception) {
            throw new Exception("In getValidThru()");
        }
    }
    public static int setPinNumber(Console console) throws Exception {
        Scanner sc = new Scanner(System.in);
        String pinNumber = null;
        boolean iterate = true;
        int iterateCount = 0;
        while (iterate) {
            char[] pinArray = console.readPassword("\n*** Create a pin number for your credit card ***");
            pinNumber = new String(pinArray);
            boolean isValidPin = ValidatePin.isPinValid(pinNumber);
            if(!isValidPin) {
                if(iterateCount < 2) {
                    System.out.println("\n\n*** Invalid Pin. " +
                            "You pin number must be either 4 digit or 6 digit long. Try Again. Attempts Remaining. " + (2 - iterateCount) + " ***");
                    iterateCount++;
                }
                else {
                    System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                    System.exit(0);
                }
            }
            else {
                iterate = false;

            }
        }
        return Integer.parseInt(pinNumber);
    }
    public static int generateSecurityCode() throws Exception {
        try {
            int min = 100; // minimum 3 digit number
            int max = 999; // maximum 3 digit number
            return (int) (Math.random() * (max - min + 1)) + min;
        }
        catch (Exception exception) {
            throw new Exception("In generateSecurityCode()");
        }
    }
    public static int getCreditLimit(CardCategory cardCategory) throws Exception {
        try {
            if(cardCategory == CardCategory.PLATINUM)
                return 50000;
            else if(cardCategory == CardCategory.GOLD)
                return 25000;
            return 10000;
        }
        catch (Exception exception) {
            throw new Exception("In getCreditLimit()");
        }
    }

    public static Date createPaymentDueDate(Date date, int days) throws Exception {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);
            return calendar.getTime();
        }
        catch (Exception exception) {
            throw new Exception("In createPaymentDueDate()");
        }
    }
}
