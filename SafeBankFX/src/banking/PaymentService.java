package banking;

import models.CardType;
import models.Customer;
import transactions.OnlinePayment;
import validations.ValidateAmount;

import java.util.Scanner;

public class PaymentService {
    public static void handlePaymentService(Customer customer) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\n*** Enter the amount to be paid ***");
        String amount = sc.next();
        boolean isAmountValid = ValidateAmount.isAmountValidForOnlinePayment(amount);
        boolean iterate = true;
        int iterateCount = 0;
        while (iterate) {
            if (isAmountValid) {
                double amountDouble = Double.parseDouble(amount);
                paymentOptions(customer, amountDouble);
            } else {
                if (iterateCount < 2) {
                    System.out.println("\n\n*** Invalid Amount (OR) Maximum Allowed Amount Exceeded. Try Again. Attempts Remaining : " + (2 - iterateCount) + " ***");
                    iterateCount++;
                } else {
                    System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                    System.exit(0);
                }
            }
        }
    }

    public static void paymentOptions(Customer customer, double amount) throws Exception {
        Scanner sc = new Scanner(System.in);
        CardType cardType = null;
        boolean iterate = true;
        int iterationCount = 0;
        while (iterate) {
            System.out.println("\n\nHow would you like to pay through ?\n1. Credit Card\n2. Debit card");
            String choice = sc.next();
            boolean isValidNumericalOption = BankingServices.isValidNumericalOption(choice, 2);
            if (isValidNumericalOption) {
                int chosenOption = Integer.parseInt(choice);
                switch (chosenOption) {
                    case 1:
                        cardType = CardType.CREDIT_CARD;
                        iterate = false;
                        break;
                    case 2:
                        iterate = false;
                        cardType = CardType.DEBIT_CARD;
                        break;
                }
                OnlinePayment onlinePayment  = new OnlinePayment(cardType, customer, amount);
                onlinePayment.start();
                onlinePayment.join();
            } else {
                if (iterationCount < 2) {
                    System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : " + (2 - iterationCount) + " ***");
                    iterationCount++;
                } else {
                    BankingServices.exitSuccessfully();
                }
            }
        }
    }

    public static void optionsAfterInsufficientFunds(Customer customer) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean iterate = true;
        int iterationCount = 0;
        while (iterate) {
            System.out.println("\n\nWhat would you like to do now ?\n1. Try again with a different amount\n2. Main Menu");
            String choice = sc.next();
            boolean isValidNumericalOption = BankingServices.isValidNumericalOption(choice, 2);
            if(isValidNumericalOption) {
                int chosenOption = Integer.parseInt(choice);
                switch (chosenOption) {
                    case 1:
                        PaymentService.handlePaymentService(customer);
                        iterate = false;
                        break;
                    case 2:
                        BankingServices.getAllServices();
                        iterate = false;
                        break;
                }
            }
            else {
                if(iterationCount < 2) {
                    System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : "+(2 - iterationCount)+" ***");
                    iterationCount++;
                }
                else {
                    BankingServices.exitSuccessfully();
                }
            }
        }
    }
}
