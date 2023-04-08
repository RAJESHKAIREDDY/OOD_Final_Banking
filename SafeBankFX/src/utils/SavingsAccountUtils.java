package utils;

public class SavingsAccountUtils {
	
	public static long generateAccountNumber() {
        long min = 10000000000L; // minimum 11 digit number
        long max = 99999999999L; // maximum 11 digit number
        long range = max - min + 1;
        return (long) (Math.random() * range) + min;
    }
}
