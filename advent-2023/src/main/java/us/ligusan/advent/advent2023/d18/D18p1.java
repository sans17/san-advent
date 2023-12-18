package us.ligusan.advent.advent2023.d18;

import us.ligusan.advent.advent2023.Point;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D18p1 {
    public static void main(final String[] args) {
        final List<Map.Entry<Character, Integer>> data;

        try (var scanner = new Scanner(D18p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                final var matchResult = Pattern.compile("([RLDU]) (\\d+)").matcher(s).results().findFirst().get();
                return Map.entry(matchResult.group(1).charAt(0), Integer.parseInt(matchResult.group(2)));
            }).collect(Collectors.toList());
        }

        data.forEach(entry -> System.out.format("%s\n", entry));

        final var tranch = new HashSet<Point>();
        Set<Point> rightSide = new HashSet<>();

        var currentPosition = new Point(0, 0);
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        for (final var dataEntry : data) {
            final var direction = dataEntry.getKey();
            for (int i = 0; i < dataEntry.getValue(); i++) {
                tranch.add(currentPosition);

                final int newX = currentPosition.x() + switch (direction) {
                    case 'R' -> 1;
                    case 'L' -> -1;
                    default -> 0;
                };
                final int newY = currentPosition.y() + switch (direction) {
                    case 'U' -> -1;
                    case 'D' -> 1;
                    default -> 0;
                };

                currentPosition = new Point(newX, newY);
                if (newX < minX) minX = newX;
                if (newX > maxX) maxX = newX;
                if (newY < minY) minY = newY;
                if (newY > maxY) maxY = newY;

                switch (direction) {
                    case 'R':
                        rightSide.add(new Point(newX, newY + 1));
                        break;

                    case 'D':
                        rightSide.add(new Point(newX - 1, newY));
                        break;

                    case 'L':
                        rightSide.add(new Point(newX, newY - 1));
                        break;

                    case 'U':
                        rightSide.add(new Point(newX + 1, newY));
                        break;
                }
            }
        }
        System.out.format("minX=%d, maxX=%d, minY=%d, maxY=%d\n", minX, maxX, minY, maxY);
        final var minXFinal = minX;
        final var maxXFinal = maxX;
        final var minYFinal = minY;
        final var maxYFinal = maxY;

        rightSide.removeAll(tranch);
        rightSide = Stream.iterate(Map.entry(new HashSet<Point>(), rightSide), setEntry -> !setEntry.getValue().isEmpty(),
                setEntry -> {
                    final var oldSet = setEntry.getKey();
                    final var additionalSet = setEntry.getValue();

                    oldSet.addAll(additionalSet);
                    additionalSet.clear();

                    oldSet.stream().forEach(point -> {
                        final var x = point.x();
                        final var y = point.y();

                        additionalSet.addAll(Arrays.asList(new Point(x + 1, y), new Point(x - 1, y), new Point(x, y + 1), new Point(x, y - 1)).stream().filter(newPoint -> {
                            final var newPointX = newPoint.x();
                            final var newPointY = newPoint.y();
                            return newPointX >= minXFinal && newPointX <= maxXFinal && newPointY >= minYFinal && newPointY <= maxYFinal;
                        }).collect(Collectors.toList()));
                    });
                    additionalSet.removeAll(tranch);
                    additionalSet.removeAll(oldSet);

                    return Map.entry(oldSet, additionalSet);
                }).reduce((a, b) -> b).get().getKey().stream().collect(Collectors.toSet());


        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                final var point = new Point(x, y);
                System.out.format("%c", tranch.contains(point) ? '#' : rightSide.contains(point) ? 'X' : '.');
            }
            System.out.println();
        }

        System.out.println(tranch.size() + rightSide.size());
    }
}
