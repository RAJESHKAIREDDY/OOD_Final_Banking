package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilFunctions {

    private static final String PASSWORD_REGEX_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})";

    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date generateDueDateForCreditCard() {
        LocalDate currentDate = LocalDate.now(); // get current date
        int currentMonth = currentDate.getMonthValue(); // get current month value
        int currentYear = currentDate.getYear(); // get current year value

        // create a LocalDate object for the 18th day of current month
        LocalDate eighteenthDayOfCurrentMonth = LocalDate.of(currentYear, currentMonth, 18);

        // create a LocalDate object for the input date
        LocalDate inputDate = LocalDate.of(2023, 3, 20); // replace with your input date

        // compare input date with 18th day of current month
        try {
            if (inputDate.isBefore(eighteenthDayOfCurrentMonth) || inputDate.isEqual(eighteenthDayOfCurrentMonth)) {
                Date seventhOfNextMonth = getSeventhOfNextMonth();
                return seventhOfNextMonth;
            } else {
                Date seventhOfNextToNextMonth = getSeventhOfNextMonth();
                Date dateAfterAddingOneMonth = addOneMonthToDate(seventhOfNextToNextMonth.toString());
                return dateAfterAddingOneMonth;
            }
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date getSeventhOfNextMonth() throws ParseException {
        LocalDate currentDate = LocalDate.now(); // get current date
        LocalDate nextMonth = currentDate.plusMonths(1); // get date of next month
        LocalDate seventhOfNextMonth = LocalDate.of(nextMonth.getYear(), nextMonth.getMonthValue(), 8); // create date for 7th of next month
        return getFullFormattedDate(seventhOfNextMonth.toString());
    }

    public static Date getFullFormattedDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(inputDate); // convert input string to Date object

        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        outputFormat.setTimeZone(TimeZone.getTimeZone("America/New_York")); // set time zone to Eastern Time (EDT)
        String outputDate = outputFormat.format(date); // format the date to desired string

        System.out.println(outputDate); // print the formatted date

        // convert formatted string to Date object
        Date outputDateObj = outputFormat.parse(outputDate);
        return outputDateObj;
    }

    public static Date addOneMonthToDate(String inputDateStr) {
        // parse input string to LocalDateTime object
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
        LocalDateTime inputDate;
        try {
            inputDate = LocalDateTime.parse(inputDateStr, inputFormat);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid input date string: " + inputDateStr);
            return null;
        }

        // add one month to input date
        LocalDateTime outputDate = inputDate.plusMonths(1).minusSeconds(1);

        // convert LocalDateTime to Date object
        Date date = java.util.Date.from(outputDate.atZone(java.time.ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[A-Z0-9_!#$%&'*+/=?`{|}~^-]+↵\n" +
                ")*@[A-Z0-9-]+(?:\\.[A-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_REGEX_PATTERN);
    }

    public static int generateRandomPinCode() {
        Random random = new Random();
        int pin = 0;
        int[] digits = {4,6};
        int randomIndex = digits[(int) Math.floor(Math.random() * 2)];
        if(randomIndex == 0)
            pin = random.nextInt(9000) + 1000;
        else
            pin = random.nextInt(900000) + 100000;

        return pin;
    }
}
