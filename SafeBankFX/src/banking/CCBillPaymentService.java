package banking;

import models.*;
import reports.CreditScore;
import validations.ValidateAmount;

import java.util.Date;
import java.util.Scanner;

public class CCBillPaymentService {
    private static final Scanner sc = new Scanner(System.in);

    public static void creditCardBillPaymentService(Customer customer) throws Exception {
        double remainingBalance = customer.getCreditCard().getRemainingCreditBalance();
        double totalCreditLimit = customer.getCreditCard().getCreditLimit();
        boolean iteration = true;
        int iterateCount = 0;
        while (iteration == true) {
            System.out.println("\n*** How much do you want to pay ? ***");
            System.out.println("\n1. Pay Total Amount\n2. Pay Minimum Amount\n3. Pay Custom Amount");
            String selectOption = sc.next();
            boolean isValidOption = BankingServices.isValidNumericalOption(selectOption, 3);
            if (isValidOption) {
                int selectedValue = Integer.parseInt(selectOption);
                switch (selectedValue) {
                    case 1:
                        payTotalAmount(customer);
                        break;
                    case 2:
                        double payableAmount = totalCreditLimit - remainingBalance;
                        double minAmount = 0.1 * payableAmount;
                        payCustomAmount(customer, minAmount + "", "min");
                        iteration = false;
                        break;
                    case 3:
                        System.out.println("\n*** Enter the amount you want to pay for your outstanding credit card bill ***");
                        String customAmount = sc.next();
                        payCustomAmount(customer, customAmount, "custom");
                        iteration = false;
                        break;
                }
            } else {
                if (iterateCount < 2) {
                    System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : " + (2 - iterateCount) + " ***");
                    iterateCount++;
                } else {
                    BankingServices.exitSuccessfully();
                }
            }
        }
    }

    private static void payTotalAmount(Customer customer) throws Exception {
        boolean iterate = true;
        int iterationCount = 0;
        while (iterate) {
            double totalCreditLimit = customer.getCreditCard().getCreditLimit();
            double remainingBalance = customer.getCreditCard().getRemainingCreditBalance();
            double totalPayableAmount = totalCreditLimit - remainingBalance;
            Date lastDueDate = customer.getCreditCard().getLastDueDate();
            Date lastPaymentDate = new Date();

            if (remainingBalance < totalCreditLimit) {
                double amountAvailableInSavings = customer.getDebitCard().getAccountBalance();
                if (totalPayableAmount <= amountAvailableInSavings) {
                    customer.getCreditCard().setRemainingCreditBalance(totalCreditLimit);
                    if (lastPaymentDate.compareTo(lastDueDate) < 0) {
                        customer.getCreditCard().setLastPaymentDate(lastPaymentDate);
                        CreditScore.setCreditScore(CreditScore.getCreditScore() + 10);
                    }
                    System.out.println("*** Successfully Paid USD " + totalPayableAmount + " towards the outstanding Credit Card Bill ***");
                    System.out.println("Updated Credit Balance : " + customer.getCreditCard().getRemainingCreditBalance());
                    Transaction transaction = new Transaction();
                    transaction.setTransactionType(TransactionType.CC_BILL_PAYMENT);
                    transaction.setTransactionMode(TransactionMode.DEBIT);
                    transaction.setAmount(totalPayableAmount);
                    transaction.setCardType(CardType.DEBIT_CARD);
                    customer.getTransactionList().getTransactions().add(transaction);
                    customer.getDebitCard().setAccountBalance(amountAvailableInSavings - totalPayableAmount);
                    BankingServices.askForContinuation();
                    iterate = false;
                } else {
                    System.out.println("\n*** Transaction Failed. Insufficient Funds in your savings account. Try Again ***");
                    boolean askForContinuation = true;
                    int iterationCountForInsufficientFunds = 0;
                    while (askForContinuation) {
                        System.out.println("\n\nHow would you like to proceed ?\n\n1. Deposit\n2. Go Back");
                        String choice = sc.next();
                        boolean isValidNumericalOption = BankingServices.isValidNumericalOption(choice, 2);
                        if (isValidNumericalOption) {
                            int chosenOption = Integer.parseInt(choice);
                            switch (chosenOption) {
                                case 1:
                                    DepositService.handleDepositService(customer);
                                    iterate = false;
                                    askForContinuation = false;
                                    break;
                                case 2:
                                    creditCardBillPaymentService(customer);
                                    iterationCount++;
                                    askForContinuation = false;
                                    break;
                            }
                        } else {
                            if (iterationCountForInsufficientFunds < 2) {
                                System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : " + (2 - iterationCount) + " ***");
                                iterationCountForInsufficientFunds++;
                            } else {
                                BankingServices.exitSuccessfully();
                            }
                        }
                    }
                }
            } else {
                System.out.println("\n*** You do not have any outstanding payable amount ***");
            }

        }
        BankingServices.askForContinuation();
    }

    private static void payCustomAmount(Customer customer, String amountStringValue, String mode) throws Exception {
        boolean iterate = true;
        int iterationCount = 0;
        double totalCreditLimit = customer.getCreditCard().getCreditLimit();
        while (iterate) {
            boolean isValidAmount = ValidateAmount.isAmountValidForCCBillPayment(amountStringValue, totalCreditLimit);
            if (isValidAmount) {
                double amount = Double.parseDouble(amountStringValue);
                double amountAvailableInSavings = customer.getDebitCard().getAccountBalance();
                if (amount <= amountAvailableInSavings) {
                    double remainingBalance = customer.getCreditCard().getRemainingCreditBalance();
                    Date lastDueDate = customer.getCreditCard().getLastDueDate();
                    Date lastPaymentDate = new Date();
                    customer.getCreditCard().setRemainingCreditBalance(remainingBalance + amount);
                    customer.getCreditCard().setLastPaymentDate(lastPaymentDate);
                    if (lastPaymentDate.compareTo(lastDueDate) < 0) {
                        CreditScore.setCreditScore(CreditScore.getCreditScore() + 10);
                    }
                    System.out.println("\n*** Successfully Paid Minimum Required Balance *** ");
                    Transaction transaction = new Transaction();
                    transaction.setTransactionType(TransactionType.CC_BILL_PAYMENT);
                    transaction.setTransactionMode(TransactionMode.DEBIT);
                    transaction.setAmount(amount);
                    transaction.setCardType(CardType.DEBIT_CARD);
                    customer.getTransactionList().getTransactions().add(transaction);
                    customer.getDebitCard().setAccountBalance(amountAvailableInSavings - amount);
                    BankingServices.askForContinuation();
                    iterate = false;
                } else {
                    if (iterationCount < 2) {
                        System.out.println("\n*** Transaction Failed. Insufficient Funds in your savings account. Try Again. Attempts Remaining : " + (2 - iterationCount) + " ***");
                        if(mode.equals("min")) creditCardBillPaymentService(customer);
                        iterationCount++;
                    } else {
                        System.out.println("\n*** Transaction Blocked. Access Denied ***");
                        System.exit(0);
                    }
                }
            } else {
                if (iterationCount < 2) {
                    System.out.println("\n\n*** Invalid Amount (OR) Maximum Allowed Amount Exceeded. Try Again. Attempts Remaining : " + (2 - iterationCount) + " ***");
                    iterationCount++;
                } else {
                    System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                    System.exit(0);
                }
            }
        }
    }

    public static void viewCreditCardBillPaymentStatus(Customer customer) throws Exception {
        CreditCard customerCreditCard = customer.getCreditCard();
        double totalCreditLimit = customerCreditCard.getCreditLimit();
        double remainingBalance = customerCreditCard.getRemainingCreditBalance();
        double totalPayableAmount = totalCreditLimit - remainingBalance;
        Date lastDueDate = customerCreditCard.getLastDueDate();
        Date lastPaymentDate = customerCreditCard.getLastPaymentDate();
        int comparedDates = lastPaymentDate.compareTo(lastDueDate);
        boolean noCreditTransactionsYet = customer.getCreatedAt().compareTo(lastDueDate) == 0;
        String payment = "";
        if (comparedDates > 0)
            payment = "Late";
        else if (comparedDates == 0)
            payment = "On Time";
        else {
            if (totalPayableAmount > 0)
                payment = "Pending";
            else
                payment = "In Time";
        }
        System.out.println("\n*********** " + customer.getName() + "'s Credit Card Bill Payment Status ***********");
        System.out.println("\nTotal Credit Limit : USD " + totalCreditLimit);
        System.out.println("Remaining Credit Balance : USD " + remainingBalance);
        System.out.println("Outstanding Payable : USD " + totalPayableAmount);
        if (!noCreditTransactionsYet) {
            System.out.println("Due Date : " + lastDueDate);
            System.out.println("Payment Date : " + lastPaymentDate);
            System.out.println("Timely / Late Payment : " + payment);
        }
        System.out.println("\n*********** " + customer.getName() + "'s Credit Card Bill Payment Status ***********");
        BankingServices.askForContinuation();
    }
}
