package validations;

public class ValidatePin {
    
	public static boolean isPinValid(String pinNumber) throws Exception {
        try {
            int parsedPin = Integer.parseInt(pinNumber);
            if(pinNumber.length() == 4 || pinNumber.length() == 6) {
                if(pinNumber.length() == 4) {
                    return parsedPin >= 1111 && parsedPin <= 9999;
                }
                else if (pinNumber.length() == 6) {
                    return parsedPin >= 111111 && parsedPin <= 999999;
                }
                return false;
            }
            return false;
        }
        catch(Exception exception) {
            return false;
        }
    }
	
	public static boolean isSecurityCodeValid(String securityCode) throws Exception {
        try {
            int parsedSecurityCode = Integer.parseInt(securityCode);
            if(securityCode.length() == 3) {
                  return parsedSecurityCode >= 111 
                		  && parsedSecurityCode <= 999;
             }
            return false;
        }
        catch(Exception exception) {
            return false;
        }
    }
}
