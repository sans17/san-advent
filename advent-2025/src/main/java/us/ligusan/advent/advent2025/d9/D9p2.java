package us.ligusan.advent.advent2025.d9;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D9p2 {
    enum DIRECTION {
        UP, RIGHT, DOWN, LEFT
    }

    enum CORNER {
        UP_RIGHT, UP_UP, UP_LEFT, RIGHT_UP, RIGHT_RIGHT, RIGHT_DOWN, DOWN_RIGHT, DOWN_DOWN, DOWN_LEFT, LEFT_DOWN, LEFT_LEFT, LEFT_UP
    }

    static void main(String[] args) {
        var filename = "input.txt";

        final List<List<Integer>> input;
        try (var scanner = new Scanner(D9p2.class.getResourceAsStream(filename))) {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).map(s -> Arrays.stream(s.trim().split(",")).map(Integer::parseInt).toList()).toList();
        }
        final var size = input.size();

        int min = Stream.iterate(0, i -> i < size, i -> i + 1).map(i -> {
            var p0 = input.get(i);
            return Map.entry(i, p0.stream().reduce(Integer::sum).orElse(0));
        }).min(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();

        var rs = Stream.iterate(0, i -> i < size - 1, i -> i + 1).flatMap(i -> {
            var p0 = input.get(i);
            return Stream.iterate(i + 1, j -> j < size, j -> j + 1).map(j -> List.of(p0, input.get(j)));
        }).collect(Collectors.toList());

        DIRECTION d = DIRECTION.UP;
        for (int i = 0; i < size; i++) {
            var j = (min + i) % size;

            System.out.printf("%d: %s %d%n", j, d, rs.size());

            var p0 = input.get(j);
            var x0 = p0.getFirst();
            var y0 = p0.getLast();

            var p1 = input.get((j + 1) % size);
            var x1 = p1.getFirst();
            var y1 = p1.getLast();

            CORNER corner = switch (d) {
                case UP -> x1 > x0 ? CORNER.UP_RIGHT : x1 < x0 ? CORNER.UP_LEFT : CORNER.UP_UP;
                case RIGHT -> y1 > y0 ? CORNER.RIGHT_DOWN : y1 < y0 ? CORNER.RIGHT_UP : CORNER.RIGHT_RIGHT;
                case DOWN -> x1 < x0 ? CORNER.DOWN_LEFT : x1 > x0 ? CORNER.DOWN_RIGHT : CORNER.DOWN_DOWN;
                case LEFT -> y1 < y0 ? CORNER.LEFT_UP : y1 > y0 ? CORNER.LEFT_DOWN : CORNER.LEFT_LEFT;
            };

            d = switch (corner) {
                case UP_RIGHT, RIGHT_RIGHT, DOWN_RIGHT -> DIRECTION.RIGHT;
                case UP_UP, RIGHT_UP, LEFT_UP -> DIRECTION.UP;
                case UP_LEFT, DOWN_LEFT, LEFT_LEFT -> DIRECTION.LEFT;
                case RIGHT_DOWN, DOWN_DOWN, LEFT_DOWN -> DIRECTION.DOWN;
            };

            var dx = switch (d) {
                case UP, DOWN -> 0;
                case RIGHT -> 1;
                case LEFT -> -1;
            };
            var dy = switch (d) {
                case RIGHT, LEFT -> 0;
                case UP -> -1;
                case DOWN -> 1;
            };
            var tl = List.of(x0 - 1, y0 - 1);
            var tr = List.of(x0 + 1, y0 - 1);
            var br = List.of(x0 + 1, y0 + 1);
            var bl = List.of(x0 - 1, y0 + 1);

            for (var op = switch (corner) {
                case UP_RIGHT -> tl;
                case UP_UP -> List.of(x0 - 1, y0);
                case UP_LEFT -> bl;
                case RIGHT_UP -> tl;
                case RIGHT_RIGHT -> List.of(x0, y0 - 1);
                case RIGHT_DOWN -> tr;
                case DOWN_RIGHT -> tr;
                case DOWN_DOWN -> List.of(x0 + 1, y0);
                case DOWN_LEFT -> br;
                case LEFT_DOWN -> br;
                case LEFT_LEFT -> List.of(x0, y0 + 1);
                case LEFT_UP -> bl;
            }; ; ) {
                var x = op.getFirst();
                var y = op.getLast();
                if (Math.abs(x - x1) + Math.abs(y - y1) <= 1) break;

                if (!onLines(op, input)) {
                    final var opFinal = op;
                    rs.removeIf(r -> inRectangle(opFinal, r.getFirst(), r.getLast()));
                }
                op = List.of(x + dx, y + dy);
            }
        }

        System.out.println(rs.stream().mapToLong(r -> {
            var p0 = r.getFirst();
            var p1 = r.getLast();
            return (long)(Math.abs(p0.getFirst() - p1.getFirst()) + 1) * (Math.abs(p0.getLast() - p1.getLast()) + 1);
        }).max().getAsLong());
    }

    static boolean inRectangle(final List<Integer> p, final List<Integer> p0, final List<Integer> p1) {
        var x = p.getFirst();
        var y = p.getLast();

        var x0 = p0.getFirst();
        var y0 = p0.getLast();
        var x1 = p1.getFirst();
        var y1 = p1.getLast();

        return Math.min(x0, x1) <= x && x <= Math.max(x0, x1) && Math.min(y0, y1) <= y && y <= Math.max(y0, y1);
    }

    static boolean onLine(final List<Integer> p, final List<Integer> p0, final List<Integer> p1) {
        var x = p.getFirst();
        var y = p.getLast();

        var x0 = p0.getFirst();
        var y0 = p0.getLast();
        var x1 = p1.getFirst();
        var y1 = p1.getLast();

        return x0.equals(x1) && x0.equals(x) && Math.min(y0, y1) <= y && y <= Math.max(y0, y1) ||
                y0.equals(y1) && y0.equals(y) && Math.min(x0, x1) <= x && x <= Math.max(x0, x1);
    }

    static boolean onLines(final List<Integer> p, final List<List<Integer>> input) {
        var size = input.size();
        return Stream.iterate(0, i -> i < size, i -> i + 1).anyMatch(i -> {
            var p0 = input.get(i);
            var p1 = input.get((i + 1) % size);
            return onLine(p, p0, p1);
        });
    }
}
