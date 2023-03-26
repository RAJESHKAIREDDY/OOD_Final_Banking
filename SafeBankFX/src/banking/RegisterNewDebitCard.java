package banking;

import models.*;
import validations.ValidatePin;

import java.io.Console;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class RegisterNewDebitCard {

    public static DebitCard registerNewDebitCard(Customer customer) throws Exception {

        try {

            Console console = System.console();
            if (console == null) {
                System.out.println("Console not available");
                System.exit(1);
            }

            DebitCard debitCard = new DebitCard();
            debitCard.setCardAssociator(CardAssociator.BANK_OF_AMEERPET);
            debitCard.setCardType(CardType.DEBIT_CARD);
            long cardNumber = generateCardNumber();
            debitCard.setCardNumber(cardNumber);
            long accountNumber = generateAccountNumber();
            debitCard.setAccountNumber(accountNumber);
            CardProvider cardProvider = getCardProvider(cardNumber + "");
            debitCard.setCardProvider(cardProvider);
            debitCard.setNameOnCard(customer.getName());
            debitCard.setCreatedAt(new Date());
            Date validThru = getValidThru(debitCard.getCreatedAt());
            debitCard.setValidThru(validThru);
            debitCard.setCardCategory(CardCategory.BURGUNDY);
            int pinNumber = setPinNumber(console);
            debitCard.setPinNumber(pinNumber);
            int securityCode = generateSecurityCode();
            debitCard.setSecurityCode(securityCode);

            System.out.println("*** Generated Debit Card ***");
            return debitCard;
        }
        catch (Exception exception) {
            throw new Exception("\n\n*** Failed to save new user details. Invalid Inputs. ***");
        }
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
            if(prefix == 4 || prefix == 5 || prefix == 6) {
                randNumStr = prefix + randNumStr.substring(1);
            }
            else if(prefix == 34 || prefix == 37) {
                randNumStr = prefix + randNumStr.substring(2);
            }
            return Long.parseLong(randNumStr);
        }
        else {
            return randomNum;
        }
    }
    public static CardProvider getCardProvider(String cardNumber) {
        if(cardNumber.startsWith("4"))
            return CardProvider.VISA;
        else if(cardNumber.startsWith("5"))
            return CardProvider.MASTER_CARD;
        else if(cardNumber.startsWith("6"))
            return CardProvider.AMERICAN_EXPRESS;
        return CardProvider.DISCOVER;
    }
    public static long generateAccountNumber() {
        long min = 10000000000L; // minimum 11 digit number
        long max = 99999999999L; // maximum 11 digit number
        long range = max - min + 1;
        return (long) (Math.random() * range) + min;
    }
    public static Date getValidThru(Date inputDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.YEAR, 5);
        return calendar.getTime();
    }
    public static int setPinNumber(Console console) throws Exception {
        Scanner sc = new Scanner(System.in);
        String pinNumber = null;
        boolean iterate = true;
        int iterateCount = 0;
        while (iterate) {
            char[] pinArray = console.readPassword("\n*** Create a pin number for your debit card ***");
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
    public static int generateSecurityCode() {
        int min = 100; // minimum 3 digit number
        int max = 999; // maximum 3 digit number
        return (int) (Math.random() * (max - min + 1)) + min;
    }

}
