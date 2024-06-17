package ir.FiveMFive.FiveMFive.Utility.Checkers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberFormatChecker {
    private static final Pattern pattern = Pattern.compile("(\\+?98)?0?(?<number>9\\d{9})");
    public static String checkFaultyNumbers(String numbers) {
        String[] numbersSplitted = numbers.split(",");

        for(String number : numbersSplitted) {
            Matcher mat = pattern.matcher(number);
            if(!mat.matches()) {
              return number;
            }
        }
        return null;
    }
    public static boolean checkNumberFormat(String number) {
        Matcher mat = pattern.matcher(number);
        return mat.matches();
    }
}
