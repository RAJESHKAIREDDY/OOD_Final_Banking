package banking;

import models.CreditCard;
import models.Customer;
import models.DebitCard;
import models.TransactionList;
import utils.UtilFunctions;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RegisterNewUser {

    public static Customer registerNewUser() throws Exception {

        try {
            Scanner sc = new Scanner(System.in);
            Console console = System.console();
            Customer customer = new Customer();
            if (console == null) {
                System.out.println("Console not available");
                System.exit(1);
            }

            System.out.println("\n*** Registering New User ***");

            int countUsername = 0;
            while (true) {
                System.out.print("\nEnter Name : ");
                String name = sc.nextLine();
                if(name == null || name.isEmpty() || name.isBlank() || name.equals("null") || name.equals("empty") || name.equals("undefined")) {
                    System.out.println("Enter a valid Username");
                    countUsername++;
                }
                else {
                    customer.setName(name);
                    break;
                }
                if(countUsername > 2) {
                    BankingServices.exitSuccessfully();
                }
            }

            int countEmail = 0;
            while (true) {
                System.out.print("\nEnter Email : ");
                String email = sc.next();
                if(!UtilFunctions.isEmailValid(email)) {
                    System.out.println("Enter a valid email");
                    countEmail++;
                }
                else {
                    customer.setEmail(email);
                    System.out.println(customer.getEmail());
                    break;
                }
                if(countEmail > 2) {
                    BankingServices.exitSuccessfully();
                }
            }

            int countPassword = 0;
            while (true) {
                char[] passwordArray = console.readPassword("\nEnter Password : ");
                String password = new String(passwordArray);
                if(!UtilFunctions.isEmailValid(password)) {
                    System.out.println("Enter a valid password");
                    countPassword++;
                }
                else {
                    customer.setPassword(password);
                    break;
                }
                if(countPassword > 2) {
                    BankingServices.exitSuccessfully();
                }
            }

            DebitCard debitCard = RegisterNewDebitCard.registerNewDebitCard(customer);
            customer.setDebitCard(debitCard);
            CreditCard creditCard = RegisterNewCreditCard.registerNewCreditCard(customer);
            customer.setCreditCard(creditCard);
            customer.setCreatedAt(new Date());

            customer.setTransactionList(new TransactionList(new ArrayList<>()));

            System.out.println("\n\n*** Details Saved Successfully ***");

            return customer;
        }
        catch (Exception exception) {

            exception.printStackTrace();
            throw new Exception("\n\n*** Failed to save new user details. Invalid Inputs. ***");
        }
    }
}
