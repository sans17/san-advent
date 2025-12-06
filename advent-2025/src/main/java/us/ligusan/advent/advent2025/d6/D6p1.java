package us.ligusan.advent.advent2025.d6;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class D6p1 {
    static void main() {
        final List<List<String>> input;
        try (var scanner = new Scanner(D6p1.class.getResourceAsStream("input.txt"))) {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).map(s -> Arrays.asList(s.trim().split("\s+"))).toList();
        }

        long result = 0;
        var operations = input.getLast();
        for (int i = 0; i < operations.size(); i++) {
            boolean add = "+".equals(operations.get(i));
            long columnResult = add ? 0 : 1;
            for (int j = 0; j < input.size() - 1; j++) {
                var value = Long.parseLong(input.get(j).get(i));
                columnResult = add ? columnResult + value : columnResult * value;
            }
            result += columnResult;
        }
        System.out.println(result);
    }
}
