package us.ligusan.advent.advent2023.d1;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class D1p2 {
    public static void main(final String[] args) {
        final var numbers = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

        try (var scanner = new Scanner(D1p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
                final var retRef = new AtomicInteger();

                System.out.format("s=%s ", s);

                final var index = new AtomicInteger();
                numbers.stream().map(n -> Map.entry(index.getAndIncrement(), s.indexOf(n))).filter(e -> e.getValue() >= 0).min(Map.Entry.comparingByValue()).ifPresentOrElse(
                        e -> retRef.set(10 * (e.getKey() % 10)),
                        () -> System.out.format("Error!!! Left number not found! %s\n", s)
                );
                System.out.format("ret=%d ", retRef.get());

                index.set(0);
                numbers.stream().map(n -> Map.entry(index.getAndIncrement(), new StringBuilder(s).reverse().indexOf(new StringBuilder(n).reverse().toString()))).filter(e -> e.getValue() >= 0).min(Map.Entry.comparingByValue()).ifPresentOrElse(
                        e -> retRef.addAndGet(e.getKey() % 10),
                        () -> System.out.format("Error!!! Right number not found!  %s\n", s)
                );
                System.out.format("ret=%d\n", retRef.get());

//                System.out.format("s=%s, ret=%d\n", s, ret, chars[i], chars[j]);

                return retRef.get();
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
