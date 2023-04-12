package validations;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TransactionValidations {

//	public static boolean isValidHighSecurityCode
	public static boolean isPinValid(String pinNumber) throws Exception {
		try {
			int parsedPin = Integer.parseInt(pinNumber);
			if (pinNumber.length() == 4 || pinNumber.length() == 6) {
				if (pinNumber.length() == 4) {
					return parsedPin >= 1111 && parsedPin <= 9999;
				} else if (pinNumber.length() == 6) {
					return parsedPin >= 111111 && parsedPin <= 999999;
				}
				return false;
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}

	public static boolean isSecurityCodeValid(String securityCode) throws Exception {
		try {
			int parsedSecurityCode = Integer.parseInt(securityCode);
			if (securityCode.length() == 3) {
				return parsedSecurityCode >= 111 && parsedSecurityCode <= 999;
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}

	private static Date getOTPExpiryTimesamp(Date inputDate) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(inputDate);
		calendar.add(Calendar.MINUTE, 1);
		return calendar.getTime();
	}

	private static boolean isOTPExpired(Date inputDate) throws Exception {
		Date expiryDate = getOTPExpiryTimesamp(inputDate);
		long timeDifference = expiryDate.getTime() - new Date().getTime();
		long timeDifferenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
		System.out.println(timeDifferenceInMinutes);
		return timeDifferenceInMinutes < 0;
	}

	public static boolean isOTPValid(String OTP, Date generatedOTPTimestamp) throws Exception {
		try {
			int parsedSecurityCode = Integer.parseInt(OTP);
			if (OTP.length() == 6) {
				return parsedSecurityCode >= 100000 && parsedSecurityCode <= 999999
						&& !isOTPExpired(generatedOTPTimestamp);
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}

	public static boolean isAmountValidForDeposit(String amount) throws Exception {
		try {
			double amountDouble = Double.parseDouble(amount);
			if (amountDouble > 0 && amountDouble % 100 == 0) {
				return amountDouble <= 20000;
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}

	public static boolean isAmountValidForOnlinePayment(String amount) throws Exception {
		try {
			double amountDouble = Double.parseDouble(amount);
			if (amountDouble > 0) {
				return amountDouble <= 100000;
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}

	public static boolean isAmountValidForCCBillPayment(String amount, double totalCardLimit) throws Exception {
		try {
			double amountDouble = Double.parseDouble(amount);
			if (amountDouble > 0) {
				return amountDouble <= totalCardLimit;
			}
			return false;
		} catch (Exception exception) {
			return false;
		}
	}
}
