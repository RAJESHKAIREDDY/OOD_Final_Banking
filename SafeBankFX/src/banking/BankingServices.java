package banking;

import models.Customer;
import models.CustomerList;
import utils.Messages;

import java.util.*;
import java.util.stream.Collectors;

public class BankingServices {
    private static final Scanner sc = new Scanner(System.in);
    private static Customer customer;
    private static boolean createdUserInThisSession;
    private static final List<Customer> customerList = new ArrayList<>();
    private static final CustomerList customers = new CustomerList(customerList);
    public static void getCustomerInstance(int triggeredFrom, int invalidCredentialsCount) throws Exception {
        if(triggeredFrom == 1) {
            try {
                customer = RegisterNewUser.registerNewUser();
            }
            catch (Exception exception) {
                throw exception;
            }
            customers.getCustomerList().add(customer);
            createdUserInThisSession = true;
        }
        else {
            Map<String, String> credentials = promptEmailPassword();
            List<Customer> filteredCustomerList = customers.getCustomerList()
                            .stream()
                            .filter(customer ->
                                    customer.getEmail().equals(credentials.get("email")) &&
                                    customer.getPassword().equals(credentials.get("password")))
                            .collect(Collectors.toList());
            if(filteredCustomerList.isEmpty()) {

                invalidCredentialsCount++;
                if(invalidCredentialsCount < 3) {
                    System.out.println("\n\n*** Entered credentials does not match any existing customer's record. " +
                            "Try Again. Attempts Remaining : " +(3 - invalidCredentialsCount)+" ***");
                    getCustomerInstance(triggeredFrom, invalidCredentialsCount);
                }
                else {
                    System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                    System.exit(0);
                }
            }
            else {
                customer = filteredCustomerList.get(0);
                System.out.println("\n\n*** Logged in as " +customer.getName()+" ***\n\n");
            }
        }
    }
    private static Map<String, String> promptEmailPassword()  throws Exception {
        Map<String, String> credentials = new HashMap<>();
        System.out.println("\n\n*** Login with your credentials to continue ***");
        boolean iterate = true;
        try {
            while (iterate == true) {
                System.out.print("\nEnter your email : ");
                String email = sc.next();
                System.out.print("Enter your password : ");
                String password = sc.next();
                if(email == null || email.isEmpty() || email.length() == 0 || email.isBlank() ||
                password == null || password.isEmpty() || password.length() == 0 || password.isBlank()) {
                    System.out.println("*** Enter valid email and password ***");
                    iterate = true;
                }
                else
                    iterate = false;
                    credentials.put("email", email);
                    credentials.put("password", password);
            }

            return credentials;
        }
        catch(Exception exception) {
            throw exception;
        }
    }
    public static void getAllServices() throws Exception {
        try {
                boolean iteration = true;
                while (iteration) {
                    System.out.println("\n\n*** Enter any number from the below list to get the relevant service ***\n");
                    String[] listOfServices = {
                            "Register New User",
                            "Withdrawl",
                            "Deposit",
                            "Online Payment",
                            "Credit Card Bill Payment",
                            "Money Transfers",
                            "View Reports",
                            "Check Balance",
                            "View All Transactions",
                            "View Credit Card Bill Status",
                            "Exit"
                    };
                    for (int i=0; i< listOfServices.length; i++) {
                        System.out.println((i+1) + ". "+listOfServices[i]);
                    }
                    String value = sc.next();
                    boolean isValidNumericalOption = isValidNumericalOption(value, listOfServices.length);
                    if(isValidNumericalOption) {
                        int selectedOption = Integer.parseInt(value);
                        displayServiceOptions(selectedOption);
                        iteration = false;
                    }
                    else {
                        System.out.println("\n*** Invalid Choice. Try Again ***");
                    }
                }

        }
        catch (InputMismatchException ime) {
            throw new InputMismatchException("\n\n*** Invalid Choice. Try again with a valid option ***\n\n");
        }
        catch (Exception exception) {
            throw exception;
        }
    }

    public static void askForContinuation() throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean askForContinuation = true;
        int iterationCount = 0;
        while (askForContinuation) {
            System.out.println("\n\nDo you wish to continue ?\n1. Yes\n2. No");
            String choice = sc.next();
            boolean isValidNumericalOption = isValidNumericalOption(choice, 2);
            if(isValidNumericalOption) {
                int chosenOption = Integer.parseInt(choice);
                switch (chosenOption) {
                    case 1: getAllServices();
                        askForContinuation = false;
                        break;
                    case 2:
                        exitSuccessfully();
                        askForContinuation = false;
                        break;
                }
            }
            else {
                if(iterationCount < 2) {
                    System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : "+(2 - iterationCount)+" ***");
                    iterationCount++;
                }
                else {
                    exitSuccessfully();
                }
            }
        }
    }
       private static void displayServiceOptions(int number) throws Exception {
              try {
                  switch (number) {
                      case 1: // REGISTER NEW USER
                          getCustomerInstance(1, 0);
                          askForContinuation();
                          break;
                      case 2: // WITHDRAWL
                          if(!createdUserInThisSession)
                              getCustomerInstance(2, 0);
                          WithdrawlService.handleWithdrawlService(customer);
                          break;
                      case 3:   // DEPOSIT
                          if(!createdUserInThisSession)
                          getCustomerInstance(3,0);
                          DepositService.handleDepositService(customer);
                          break;
                      case 4:   // ONLINE PAYMENT
                          if(!createdUserInThisSession)
                              getCustomerInstance(4,0);
                          PaymentService.handlePaymentService(customer);
                          break;
                      case 5:   //CC BILL PAYMENT
                          if(!createdUserInThisSession)
                              getCustomerInstance(5,0);
                          CCBillPaymentService.creditCardBillPaymentService(customer);
                          break;
                      case 6:   //Money Transfers
                          System.out.println("\n*** Money Transfers Feature Under Development ***");
                          askForContinuation();
                          break;
                      case 7:   //View Reports
                          System.out.println("\n*** View Reports Feature Under Development ***");
                          askForContinuation();
                          break;
                      case 8: //Check Balance
                          if(!createdUserInThisSession)
                              getCustomerInstance(8,0);
                          ViewAllTransactions.checkBalance(customer);
                          break;
                      case 9:  //VIEW ALL TRANSACTIONS
                          if(!createdUserInThisSession)
                              getCustomerInstance(10,0);
                          ViewAllTransactions.viewAllTransactions(customer);
                          break;
                      case 10:  //VIEW CC BILL PAYMENT STATUS
                          if(!createdUserInThisSession)
                              getCustomerInstance(11,0);
                          CCBillPaymentService.viewCreditCardBillPaymentStatus(customer);
                          break;
                      case 11: //EXIT
                          exitSuccessfully();
                          break;
                  }
              }
              catch (Exception exception) {
                  throw exception;
              }
           }

           public static boolean isValidNumericalOption(String value, int numberOfOptions) {
                try {
                    int option = Integer.parseInt(value);
                    return option >= 1 && option <= numberOfOptions;
                }
                catch (Exception exception) {
                    return false;
                }
           }

           public static void exitSuccessfully() {
               System.out.println(Messages.THANK_YOU_MESSAGE);
               System.exit(0);
           }
    }
