package us.ligusan.advent.advent2024.d6;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class D6p1 {
    public static void main(final String[] args) throws Exception {
        final List<List<Character>> data;

        final var xIndex = new AtomicInteger();
        final var yIndex = new AtomicInteger();

        final var xStart = new AtomicInteger();
        final var yStart = new AtomicInteger();

        try (var scanner = new Scanner(D6p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                xIndex.set(0);

                final var list = s.chars().mapToObj(i -> {
                    final var c = (char) i;
                    if (c != '.' && c != '#') {
                        xStart.set(xIndex.get());
                        yStart.set(yIndex.get());
                    }

                    xIndex.getAndIncrement();
                    return c;
                }).toList();
                yIndex.incrementAndGet();

                return list;
            }).toList();
        }

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });
        System.out.format("xStart=%d, yStart=%d%n", xStart.get(), yStart.get());

        var xSize = xIndex.get();
        var ySize = yIndex.get();
        var x = xStart.get();
        var y = yStart.get();

        System.out.format("xSize=%d, ySize=%d%n", xSize, ySize);
        System.out.format("x=%d, y=%d%n", x, y);

        for (; ; ) {
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
        System.out.format("x=%d, y=%d%n", x, y);

        data.stream().mapToInt(list -> (int) list.stream().filter(c -> c != '#' && c != '.').count()).reduce(Integer::sum).ifPresent(System.out::println);
    }
}
