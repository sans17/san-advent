package us.ligusan.advent.advent2023.d2;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class D2p1 {
    public static void main(final String[] args) {
        final var limits = Map.of("red", 12, "green", 13, "blue", 14);
        final var gamePattern = Pattern.compile("Game (\\d+):");
        final var colorPattern = Pattern.compile("(\\d+) (red|green|blue)");

        try (var scanner = new Scanner(D2p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> colorPattern.matcher(s).results().anyMatch(m -> Integer.parseInt(m.group(1)) > limits.get(m.group(2))) ? 0 : Integer.parseInt(gamePattern.matcher(s).results().findFirst().get().group(1))).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
