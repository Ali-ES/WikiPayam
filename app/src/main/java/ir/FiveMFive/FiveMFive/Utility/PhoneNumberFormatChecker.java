package ir.FiveMFive.FiveMFive.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberFormatChecker {
    public static String checkFaultyNumber(String numbers) {
        String[] numbersSplitted = numbers.split(",");

        Pattern pattern = Pattern.compile("(\\+?98)?(<number>0?9\\d{9})");

        for(String number : numbersSplitted) {
            Matcher mat = pattern.matcher(number);
            if(!mat.matches()) {
              return number;
            }
        }
        return null;
    }
}
