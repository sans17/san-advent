package us.ligusan.advent.advent2024.d7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class D7p1 {
    public static void main(final String[] args) throws Exception {
        var resultsList = new ArrayList<BigInteger>();
        var operandsList = new ArrayList<List<BigInteger>>();

        try (var scanner = new Scanner(D7p1.class.getResourceAsStream("input.txt"))) {
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
            for (var j = BigInteger.ZERO; j.compareTo(BigInteger.valueOf(2).pow(operandsSize - 1)) < 0; j = j.add(BigInteger.ONE)) {
                var testResult = operands.getFirst();
                int comparisonResult = 0;

                for (int k = 1; k < operandsSize; k++) {
                    var testBit = j.testBit(k - 1);
                    var operand = operands.get(k);
                    testResult = testBit ? testResult.add(operand) : testResult.multiply(operand);
                    System.out.format("k=%d, testBit=%b, operand=%s, testResult=%s%n", k, testBit, operand, testResult);
                    comparisonResult = result.compareTo(testResult);
                    if (comparisonResult < 0) break;
                }
                if (comparisonResult == 0) {
                    counter = counter.add(result);
                    System.out.format("j=%s, counter=%s%n", j, counter);
                    break;
                }
            }
            System.out.println();
        }
        System.out.println(counter);
    }
}
