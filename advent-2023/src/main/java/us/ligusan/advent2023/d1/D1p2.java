package us.ligusan.advent2023.d1;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class D1p2 {
    public static void main(final String[] args) {
        final var numbers = Map.ofEntries(
                Map.entry("zero", 0),
                Map.entry("one", 1),
                Map.entry("two", 2),
                Map.entry("three", 3),
                Map.entry("four", 4),
                Map.entry("five", 5),
                Map.entry("six", 6),
                Map.entry("seven", 7),
                Map.entry("eight", 8),
                Map.entry("nine", 9),
                Map.entry("0", 0),
                Map.entry("1", 1),
                Map.entry("2", 2),
                Map.entry("3", 3),
                Map.entry("4", 4),
                Map.entry("5", 5),
                Map.entry("6", 6),
                Map.entry("7", 7),
                Map.entry("8", 8),
                Map.entry("9", 9)
        );

        try (var scanner = new Scanner(D1p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
                final var retReference = new AtomicReference<Integer>();

                System.out.format("s=%s ", s);

                numbers.entrySet().stream().map(e -> Map.entry(e.getKey(), s.indexOf(e.getKey()))).filter(e -> e.getValue() >= 0).min(Map.Entry.comparingByValue()).ifPresentOrElse(
                        e -> retReference.set(10 * numbers.get(e.getKey())),
                        () -> System.out.format("No number found in %s\n", s)
                );
                System.out.format("ret=%d ", retReference.get());
                numbers.entrySet().stream().map(e -> Map.entry(e.getKey(), new StringBuilder(s).reverse().indexOf(new StringBuilder(e.getKey()).reverse().toString()))).filter(e -> e.getValue() >= 0).min(Map.Entry.comparingByValue()).ifPresentOrElse(
                        e -> retReference.set(retReference.get() + numbers.get(e.getKey())),
                        () -> System.out.format("No number found in %s\n", s)
                );
                System.out.format("ret=%d ", retReference.get());
                System.out.println();

//                System.out.format("s=%s, ret=%d\n", s, ret, chars[i], chars[j]);

                return retReference.get();
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
