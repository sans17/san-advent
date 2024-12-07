package us.ligusan.advent.advent2024.d7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class D7p2 {
    public static void main(final String[] args) throws Exception {
        var resultsList = new ArrayList<BigInteger>();
        var operandsList = new ArrayList<List<BigInteger>>();

        try (var scanner = new Scanner(D7p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                var split = s.split(":");
                resultsList.add(new BigInteger(split[0]));
                operandsList.add(Arrays.stream(split[1].trim().split("\\s+")).map(BigInteger::new).toList());
            });
        }

        var counter = BigInteger.ZERO;
        for (int i = 0; i < resultsList.size(); i++) {
            var result = resultsList.get(i);
            var operands = operandsList.get(i);
            var operandsSize = operands.size();
            System.out.format("i=%d, result=%s, operands=%s%n", i, result, operands);
            for (var j = BigInteger.ZERO; j.compareTo(BigInteger.valueOf(3).pow(operandsSize - 1)) < 0; j = j.add(BigInteger.ONE)) {
                var testResult = operands.getFirst();
                int comparisonResult = 0;

                var s = "0".repeat(operandsSize - 1 - j.toString(3).length()) + j.toString(3);
                for (int k = 1; k < operandsSize; k++) {
                    var operand = operands.get(k);
                    var testChar = s.charAt(k - 1);
                    testResult = switch (testChar) {
                        case '0' -> testResult.multiply(operand);
                        case '1' -> new BigInteger(testResult.toString() + operand.toString());
                        case '2' -> testResult.add(operand);
                        default -> throw new IllegalStateException();
                    };
//                    System.out.format("k=%d, testChar=%c, operand=%s, testResult=%s%n", k, testChar, operand, testResult);
                    comparisonResult = result.compareTo(testResult);
                    if (comparisonResult < 0) break;
                }
                if (comparisonResult == 0) {
                    counter = counter.add(result);
                    System.out.format("j=%s, s=%s, counter=%s%n", j, s, counter);
                    break;
                }
            }
            System.out.println();
        }
        System.out.println(counter);
    }
}
