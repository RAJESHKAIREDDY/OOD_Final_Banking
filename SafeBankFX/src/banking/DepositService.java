package banking;

import models.Customer;
import transactions.DepositTransaction;
import validations.ValidateAmount;

import java.util.Scanner;

public class DepositService {
    private static final Scanner sc = new Scanner(System.in);
    public static void handleDepositService(Customer customer) throws Exception {
            boolean iterate = true;
            int iterateCount = 0;
            while (iterate) {
                System.out.println("\n\n*** Enter the amount to be deposited ***");
                String amount = sc.next();
                try {
                    boolean isAmountValid = ValidateAmount.isAmountValid(amount);

                    if (isAmountValid) {
                        double parsedAmount = Double.parseDouble(amount);
                        DepositTransaction depositTransaction = new DepositTransaction(customer, parsedAmount);
                        depositTransaction.start();
                        depositTransaction.join();
                        iterate = false;
                    } else {
                        if(iterateCount < 2) {
                            System.out.println("\n\n*** Invalid Amount (OR) Maximum Allowed Amount Exceeded. Try Again. Attempts Remaining : "+(2 - iterateCount)+" ***");
                            iterateCount++;
                        }
                        else {
                            System.out.println("\n\n*** Transaction Blocked. Access Denied ***");
                            System.exit(0);
                        }
                    }

                } catch (Exception exception) {
                    throw new Exception("\n\n*** Invalid Amount for Deposit. Transaction Failed ***");
                }
            }
    }
}
