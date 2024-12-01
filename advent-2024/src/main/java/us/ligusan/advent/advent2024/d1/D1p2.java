package us.ligusan.advent.advent2024.d1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class D1p2 {
    public static void main(final String[] args) {
        final var left = new ArrayList<Integer>();

        final var rightCount = new HashMap<Integer, Integer>();

        try (var scanner = new Scanner(D1p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                Pattern.compile("(\\d+)\\s+(\\d+)").matcher(s).results().forEach(m -> {
                    left.add(Integer.parseInt(m.group(1)));
                    rightCount.compute(Integer.parseInt(m.group(2)), (k, v) -> v == null ? 1 : v + 1);
                });
            });
        }
        System.out.format("left=%s%nrightCount=%s%n", left, rightCount);

        left.stream().mapToInt(i -> i * rightCount.getOrDefault(i, 0)).reduce(Integer::sum).ifPresent(System.out::println);
    }
}
