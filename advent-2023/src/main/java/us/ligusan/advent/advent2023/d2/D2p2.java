package us.ligusan.advent.advent2023.d2;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D2p2 {
    public static void main(final String[] args) {
        final var colorPattern = Pattern.compile("(\\d+) (red|green|blue)");

        try (var scanner = new Scanner(D2p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
                System.out.format("s=%s\n", s);
                final var ret = colorPattern.matcher(s).results().collect(Collectors.groupingBy(matchResult -> matchResult.group(2), Collectors.reducing(0, matchResult -> Integer.parseInt(matchResult.group(1)), Integer::max))).values().stream().reduce((a, b) -> a * b).get();
                System.out.format("ret=%d\n", ret);
                return ret;
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
