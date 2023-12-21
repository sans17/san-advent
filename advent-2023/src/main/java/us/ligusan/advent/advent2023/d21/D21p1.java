package us.ligusan.advent.advent2023.d21;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import us.ligusan.advent.advent2023.Direction;
import us.ligusan.advent.advent2023.Point;

public class D21p1 {
    public static void main(final String[] args) {
        List<List<Character>> data;

        final var positions = new HashSet<Point>();

        final var yRef = new AtomicInteger();
        try (var scanner = new Scanner(D21p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                final var y = yRef.getAndIncrement();

                final var xRef = new AtomicInteger();
                return s.chars().mapToObj(i -> {
                    final var x = xRef.getAndIncrement();

                    final var ret = (char)i;
                    if (ret == 'S') {
                        positions.add(new Point(x, y));
                    }
                    return ret;
                }).collect(Collectors.toList());
            }).collect(Collectors.toList());
        }

        final var sizeY = data.size();
        final var sizeX = data.get(0).size();

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        Set<Point> currentPositions = positions;
        for (int i = 0 ; i < 64 ; i++) {
            currentPositions = currentPositions.stream().flatMap(point -> {
                final var x = point.x();
                final var y = point.y();
                return Stream.of(new Point(x + 1, y), new Point(x - 1, y), new Point(x, y + 1), new Point(x, y - 1))
                 .filter(newPoint -> {
                     final var newX = newPoint.x();
                     final var newY = newPoint.y();
                     return newX >= 0 && newX < sizeX && newY >= 0 && newY < sizeY;
                 }).filter(newPoint -> {
                     final var c = data.get(newPoint.y()).get(newPoint.x());
                     return c == '.' || c == 'S';
                 });
            }).collect(Collectors.toSet());
        }
        System.out.println(currentPositions.size());
    }
}
