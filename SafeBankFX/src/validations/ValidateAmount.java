package validations;

public class ValidateAmount {
    public static boolean isAmountValid(String amount) throws Exception {
        try {
            double amountDouble = Double.parseDouble(amount);
            if(amountDouble > 0 && amountDouble % 100 == 0) {
                return amountDouble <= 10000;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean isAmountValidForOnlinePayment(String amount) throws Exception {
        try {
            double amountDouble = Double.parseDouble(amount);
            if(amountDouble > 0) {
                return amountDouble <= 100000;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean isAmountValidForCCBillPayment(String amount, double totalCardLimit) throws Exception {
        try {
            double amountDouble = Double.parseDouble(amount);
            if(amountDouble > 0) {
                return amountDouble <= totalCardLimit;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }
}
