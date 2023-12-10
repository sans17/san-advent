package us.ligusan.advent.advent2023.d10;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D10p1 {
    public static void main(final String[] args) {
        final List<List<Character>> data;

        final var xIndex = new AtomicInteger();
        final var yIndex = new AtomicInteger();

        final var xStart = new AtomicInteger();
        final var yStart = new AtomicInteger();

        try (var scanner = new Scanner(D10p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                xIndex.set(0);

                final var list = s.chars().mapToObj(i -> {
                    final var c = (char) i;
                    if (c == 'S') {
                        xStart.set(xIndex.get());
                        yStart.set(yIndex.get());
                    }

                    xIndex.getAndIncrement();
                    return c;
                }).collect(Collectors.toList());
                yIndex.incrementAndGet();

                return list;
            }).collect(Collectors.toList());
        }

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });
        System.out.format("xStart=%d, yStart=%d\n", xStart.get(), yStart.get());

        Stream.of(Map.entry(0, 1), Map.entry(0, -1), Map.entry(1, 0), Map.entry(-1, 0)).map(entry -> {
            System.out.format("entry=%s\n", entry);

            final var path = new StringBuilder();
            var currentPosition = Map.entry(xStart.get(), yStart.get());
            var currentDirection = entry;
            while (true) {
                System.out.format("\tcurrentPosition=%s, currentDirection=%s\n", currentPosition, currentDirection);

                final var nextX = currentPosition.getKey() + currentDirection.getKey();
                final var nextY = currentPosition.getValue() + currentDirection.getValue();
                if (nextX < 0 || nextX >= data.get(0).size() || nextY < 0 && nextY >= data.size()) break;

                final var nextC = data.get(nextY).get(nextX);
                System.out.format("\tnextC=%c\n", nextC);

                path.append(nextC);

                if (nextC == 'S') break;

                if (currentDirection.equals(Map.entry(0, 1)) && !Arrays.asList('|', 'L', 'J').contains(nextC)) break;
                if (currentDirection.equals(Map.entry(0, -1)) && !Arrays.asList('|', '7', 'F').contains(nextC)) break;
                if (currentDirection.equals(Map.entry(1, 0)) && !Arrays.asList('-', '7', 'J').contains(nextC)) break;
                if (currentDirection.equals(Map.entry(-1, 0)) && !Arrays.asList('-', 'L', 'F').contains(nextC)) break;

                currentPosition = Map.entry(nextX, nextY);
                currentDirection = currentDirection.equals(Map.entry(0, 1)) ? switch (nextC) {
                    case '|' -> Map.entry(0, 1);
                    case 'L' -> Map.entry(1, 0);
                    default -> Map.entry(-1, 0);
                } : currentDirection.equals(Map.entry(0, -1)) ? switch (nextC) {
                    case '|' -> Map.entry(0, -1);
                    case '7' -> Map.entry(-1, 0);
                    default -> Map.entry(1, 0);
                } : currentDirection.equals(Map.entry(1, 0)) ? switch (nextC) {
                    case '-' -> Map.entry(1, 0);
                    case '7' -> Map.entry(0, 1);
                    default -> Map.entry(0, -1);
                } : switch (nextC) {
                    case '-' -> Map.entry(-1, 0);
                    case 'L' -> Map.entry(0, -1);
                    default -> Map.entry(0, 1);
                };
            }
            return path.toString();
        }).filter(path -> path.endsWith("S")).max((a, b) -> a.length() - b.length()).ifPresent(path -> System.out.format("%d\n", path.length() / 2));
    }
}
