package us.ligusan.advent.advent2025.d6;

import javax.swing.*;
import java.util.*;

public class D6p2 {
    static void main() {
        final List<String> input;
        try (var scanner = new Scanner(D6p2.class.getResourceAsStream("input.txt"))) {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).toList();
        }
        final var size = input.size();

        long result = 0;

        List<Long> values = null;
        boolean add = false;
        var operations = input.getLast();
        var charsSize = operations.length();
        for (int i = 0; i < charsSize; i++) {
            var operation = operations.charAt(i);
            if (operation != ' ') {
                values = new ArrayList<>();
                add = operation == '+';
            }

            var continueFlag = false;
            var value = 0L;
            for (int j = 0; j < size - 1; j++) {
                var c = input.get(j).charAt(i);
                if (c != ' ') {
                    continueFlag = true;
                    value = value * 10 + c - '0';
                }
            }
            if (continueFlag) {
                values.add(value);
                if (i < charsSize - 1) continue;
            }

            System.out.printf("%d %b: %s%n", i, add, values);

            var valuesStream = values.stream();
            final boolean addFinal = add;
            result += valuesStream.reduce(add ? 0L : 1L, (a, b) -> addFinal ? a + b : a * b);
        }
        System.out.println(result);
    }
}
