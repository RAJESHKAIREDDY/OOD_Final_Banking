package banking;

import models.Customer;
import transactions.WithdrawlTransaction;
import validations.ValidateAmount;

import java.util.Scanner;

public class WithdrawlService {

    public static void handleWithdrawlService(Customer customer) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean iterate = true;
        int iterateCount = 0;
        int iterateCountForInsufficientFunds = 0;
        while (iterate) {
            System.out.println("\n\n*** Enter the amount to be withdrawn ***");
            String amount = sc.next();
            boolean isAmountValid = ValidateAmount.isAmountValid(amount);
            if (isAmountValid) {
                double amountDouble = Double.parseDouble(amount);
                double accountBalance = customer.getDebitCard().getAccountBalance();
                if (amountDouble <= accountBalance) {
                    WithdrawlTransaction withdrawlTransaction = new WithdrawlTransaction(customer, amountDouble);
                    withdrawlTransaction.start();
                    withdrawlTransaction.join();
                    iterate = false;
                } else {
                    if (iterateCountForInsufficientFunds < 2) {
                        System.out.println("\n\n*** Insufficient Funds. Transaction Declined. Attempts Remaining : " + (2 - iterateCountForInsufficientFunds));
                        boolean askForContinuation = true;
                        int iterationCount = 0;
                        while (askForContinuation) {
                            System.out.println("\n\nHow would you like to proceed ?\n\n1. Make a Deposit\n2. Try again with a different amount");
                            String choice = sc.next();
                            boolean isValidNumericalOption = BankingServices.isValidNumericalOption(choice, 2);
                            if (isValidNumericalOption) {
                                int chosenOption = Integer.parseInt(choice);
                                switch (chosenOption) {
                                    case 1:
                                        iterate = false;
                                        DepositService.handleDepositService(customer);
                                        askForContinuation = false;
                                        break;
                                    case 2:
                                        iterateCountForInsufficientFunds++;
                                        askForContinuation = false;
                                        break;
                                }
                            } else {
                                if (iterationCount < 2) {
                                    System.out.println("\n*** Invalid Choice. Try again. Attempts Remaining : " + (2 - iterationCount) + " ***");
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
}
