package transactions;

import banking.BankingServices;
import banking.DepositService;
import banking.PaymentService;
import models.*;
import reports.CreditScore;
import utils.UtilFunctions;
import validations.ValidatePin;

import java.io.Console;
import java.util.Date;
import java.util.Scanner;

public class OnlinePayment extends Thread {
    double amount;
    Customer customer;
    CardType cardType;

    public OnlinePayment(CardType cardType, Customer customer, double amount) {
        this.amount = amount;
        this.customer = customer;
        this.cardType = cardType;
    }

    private final Payment payment = (double amount) -> {

        boolean iterate = true;
        int iterateCountForInvalid = 0;
        int iterateCountForIncorrect = 0;
        int iterateCountForInsufficientFunds = 0;
        if (cardType == CardType.CREDIT_CARD) {
            double accountBalance = customer.getCreditCard().getRemainingCreditBalance();
            while (iterate) {
                Console console = System.console();
                if (console == null) {
                    System.out.println("Console not available");
                    System.exit(1);
                }
                char[] pinArray = console.readPassword("n\n*** Enter your credit card pin number ***");
                String pin = new String(pinArray);
                boolean isPinValid = ValidatePin.isPinValid(pin);
                if (isPinValid) {
                    int pinNumber = Integer.parseInt(pin);
                    if (pinNumber == customer.getCreditCard().getPinNumber()) {
                        if(amount <= customer.getCreditCard().getRemainingCreditBalance()) {
                            accountBalance = accountBalance - amount;
                            customer.getCreditCard().setRemainingCreditBalance(accountBalance);
                            customer.getCreditCard().setLastDueDate(UtilFunctions.generateDueDateForCreditCard());
                            double remainingBalance = customer.getCreditCard().getRemainingCreditBalance();
                            double actualCreditLimit = customer.getCreditCard().getCreditLimit();
                            System.out.println("\n\n*** Successful Online Payment of USD " + amount + " from Credit Card Number : " + customer.getCreditCard().getCardNumber() + " ***");
                            System.out.println("Updated Credit Balance : USD " + remainingBalance);
                            if (amount > 0.33 * actualCreditLimit || remainingBalance < 0.33 * actualCreditLimit) {
                                CreditScore.setCreditScore(CreditScore.getCreditScore() - 20);
                            }
                            Transaction transaction = new Transaction();
                            transaction.setTransactionType(TransactionType.ONLINE_PAYMENT);
                            transaction.setTransactionMode(TransactionMode.DEBIT);
                            transaction.setAmount(amount);
                            transaction.setCardType(CardType.CREDIT_CARD);
                            customer.getTransactionList().getTransactions().add(transaction);
                            BankingServices.askForContinuation();
                            iterate = false;
                        }
                        else {
                            if (iterateCountForInsufficientFunds < 2) {
                                System.out.println("\n\n*** Insufficient Funds. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInsufficientFunds));
                                iterateCountForInsufficientFunds++;
                                PaymentService.handlePaymentService(customer);
                                iterate = false;
                            } else {
                                System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                                System.exit(0);
                            }
                        }
                    } else {
                        if (iterateCountForIncorrect < 2) {
                            System.out.println("\n\n*** Incorrect Pin. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForIncorrect));
                            iterateCountForIncorrect++;
                        } else {
                            System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                            System.exit(0);
                        }
                    }
                } else {
                    if (iterateCountForInvalid < 2) {
                        System.out.println("\n\n*** Invalid Pin. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInvalid));
                        iterateCountForInvalid++;
                    } else {
                        System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                        System.exit(0);
                    }
                }
            }
        } else {
            double accountBalance = customer.getDebitCard().getAccountBalance();
            while (iterate) {
                Scanner sc = new Scanner(System.in);
                Console console = System.console();
                if (console == null) {
                    System.out.println("Console not available");
                    System.exit(1);
                }
                char[] pinArray = console.readPassword("n\n*** Enter your debit card pin number ***");
                String pin = new String(pinArray);
                boolean isPinValid = ValidatePin.isPinValid(pin);
                if (isPinValid) {
                    int pinNumber = Integer.parseInt(pin);
                    if (pinNumber == customer.getDebitCard().getPinNumber()) {
                        if(amount < accountBalance) {
                            accountBalance = accountBalance - amount;
                            customer.getDebitCard().setAccountBalance(accountBalance);
                            System.out.println("\n\n*** Successful Online Payment of USD " + amount + " from Savings A/C : " + customer.getDebitCard().getAccountNumber() + " ***");
                            System.out.println("Updated Balance : USD " + customer.getDebitCard().getAccountBalance());
                            Transaction transaction = new Transaction();
                            transaction.setTransactionType(TransactionType.ONLINE_PAYMENT);
                            transaction.setTransactionMode(TransactionMode.DEBIT);
                            transaction.setAmount(amount);
                            transaction.setCardType(CardType.DEBIT_CARD);
                            customer.getTransactionList().getTransactions().add(transaction);
                            BankingServices.askForContinuation();
                            iterate = false;
                        }
                        else {
                            if (iterateCountForInsufficientFunds < 2) {
                                System.out.println("\n\n*** Insufficient Funds. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInsufficientFunds));
                                boolean askForContinuation = true;
                                int iterationCount = 0;
                                while (askForContinuation) {
                                    System.out.println("\n\nHow would you like to proceed ?\n\n1. Switch Payment Options\n2. Make a Deposit to Debit Card\n3. Try again with a different amount using Debit Card");
                                    String choice = sc.next();
                                    boolean isValidNumericalOption = BankingServices.isValidNumericalOption(choice, 3);
                                    if (isValidNumericalOption) {
                                        int chosenOption = Integer.parseInt(choice);
                                        switch (chosenOption) {
                                            case 1:
                                                iterate = false;
                                                askForContinuation = false;
                                                break;
                                            case 2:
                                                iterate = false;
                                                DepositService.handleDepositService(customer);
                                                askForContinuation = false;
                                                break;
                                            case 3:
                                                iterateCountForInsufficientFunds++;
                                                askForContinuation = false;
                                                break;
                                        }
                                    } else {
                                        if (iterationCount < 2) {
                                            System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : " + (2 - iterationCount) + " ***");
                                            PaymentService.optionsAfterInsufficientFunds(customer);
                                            iterationCount++;
                                        } else {
                                            BankingServices.exitSuccessfully();
                                        }
                                    }
                                }

                            } else {
                                System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                                System.exit(0);
                            }
                        }
                    } else {
                        if (iterateCountForIncorrect < 2) {
                            System.out.println("\n\n*** Incorrect Pin. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForIncorrect));
                            iterateCountForIncorrect++;
                        } else {
                            System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                            System.exit(0);
                        }
                    }
                } else {
                    if (iterateCountForInvalid < 2) {
                        System.out.println("\n\n*** Invalid Pin. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInvalid));
                        iterateCountForInvalid++;
                    } else {
                        System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                        System.exit(0);
                    }
                }
            }
        }
    };

    @Override
    public void run() {
            try {
                synchronized (this) {
                    payment.processPayment(amount);
                }
            } catch (Exception e) {
                System.out.println("\n\n*** Transaction Failed. Could Not Pay The Amount ***");
            }
    }
}
