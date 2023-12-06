package us.ligusan.advent.advent2023.d6;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D6p1 {
    public static void main(final String[] args) {
        final List<List<Integer>> data;

        try (var scanner = new Scanner(D6p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> Pattern.compile("\\d+").matcher(s.split(":")[1]).results().map(matchResult -> Integer.parseInt(matchResult.group())).collect(Collectors.toList())).collect(Collectors.toList());
        }
        System.out.format("data=%s\n", data);

        final var indexRef = new AtomicInteger();
        data.get(0).stream().mapToLong(time -> {
            final var index = indexRef.getAndIncrement();
            return Stream.iterate(0, i -> i <= time, i -> i + 1).map(i -> (time - i) * i).filter(i -> i > data.get(1).get(index)).count();
        }).reduce((a, b) -> a * b).ifPresent(System.out::println);
    }
}
