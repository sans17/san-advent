package us.ligusan.advent.advent2024.d6;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class D6p2 {
    public static void main(final String[] args) throws Exception {
        List<List<Character>> data;

        var xIndex = new AtomicInteger();
        var yIndex = new AtomicInteger();

        var xStart = new AtomicInteger();
        var yStart = new AtomicInteger();
        var initialDirectionRef = new AtomicInteger();

        try (var scanner = new Scanner(D6p2.class.getResourceAsStream("testInput.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                xIndex.set(0);

                final var list = s.chars().mapToObj(i -> {
                    final var c = (char) i;
                    if (c != '.' && c != '#') {
                        xStart.set(xIndex.get());
                        yStart.set(yIndex.get());
                        initialDirectionRef.set(c);
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
        var initialDirection = (char) initialDirectionRef.get();
        System.out.format("xStart=%d, yStart=%d, initialDirection=%c%n", xStart.get(), yStart.get(), initialDirection);


        var xSize = xIndex.get();
        var ySize = yIndex.get();

        for (int x = xStart.get(), y = yStart.get(); ; ) {
            var newX = x;
            var newY = y;
            var direction = data.get(y).get(x);
            switch (direction) {
                case '^' -> newY--;
                case 'v' -> newY++;
                case '<' -> newX--;
                case '>' -> newX++;
            }
            if (newX < 0 || newX >= xSize || newY < 0 || newY >= ySize) break;
            if (data.get(newY).get(newX) == '#') direction = switch (direction) {
                case '^' -> '>';
                case 'v' -> '<';
                case '<' -> '^';
                case '>' -> 'v';
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
            else {
                x = newX;
                y = newY;
            }
            data.get(y).set(x, direction);
        }

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        var count = 0;
        for (int i = 0; i < xSize; i++)
            for (int j = 0; j < ySize; j++) {
                var x = xStart.get();
                var y = yStart.get();
                if ((i != x || j != y) && !List.of('.', '#').contains(data.get(j).get(i))) {
                    var testData = data.stream().map(list -> list.stream().map(c -> c == '#' ? null : new HashSet<Character>()).collect(Collectors.toList())).collect(Collectors.toList());
                    testData.get(y).get(x).add(initialDirection);
                    testData.get(j).set(i, null);

                    for (var direction = initialDirection; ; ) {
                        var newX = x;
                        var newY = y;
                        switch (direction) {
                            case '^' -> newY--;
                            case 'v' -> newY++;
                            case '<' -> newX--;
                            case '>' -> newX++;
                        }
                        if (newX < 0 || newX >= xSize || newY < 0 || newY >= ySize) break;
                        if (testData.get(newY).get(newX) == null) direction = switch (direction) {
                            case '^' -> '>';
                            case 'v' -> '<';
                            case '<' -> '^';
                            case '>' -> 'v';
                            default -> throw new IllegalStateException("Unexpected value: " + direction);
                        };
                        else {
                            x = newX;
                            y = newY;
                        }
                        if (!testData.get(y).get(x).add(direction)) {
                            count++;

                            System.out.format("i=%d, j=%d, count=%d%n", i, j, count);
                            testData.forEach(list -> {
                                list.forEach(s -> System.out.format("%c", s == null ? '#' : s.isEmpty() ? '.' : '+'));
                                System.out.println();
                            });

                            break;
                        }
                    }
                }
            }
        System.out.println(count);
    }
}
