package us.ligusan.advent.advent2023.d1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D1t1 {
    public static void main(String[] args) {
        String input = "eightwo";
        String firstDigit = extractFirstDigit(input);
        String lastDigit = extractLastDigit(input);

        System.out.println("First Digit: " + firstDigit);
        System.out.println("Last Digit: " + lastDigit);
    }

    private static String extractFirstDigit(String input) {
        Pattern digitPattern = Pattern.compile("\\d|zero|one|two|three|four|five|six|seven|eight|nine");
        Matcher matcher = digitPattern.matcher(input.toLowerCase());

        while (matcher.find()) {
            return matcher.group();
        }

        return "No digit found";
    }

    private static String extractLastDigit(String input) {
        Pattern digitPattern = Pattern.compile("\\d|zero|one|two|three|four|five|six|seven|eight|nine");
        Matcher matcher = digitPattern.matcher(input.toLowerCase());

        String lastDigit = "No digit found";
        while (matcher.find()) {
            lastDigit = matcher.group();
        }

        return lastDigit;
    }
}
