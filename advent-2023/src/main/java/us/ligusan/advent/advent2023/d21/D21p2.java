package us.ligusan.advent.advent2023.d21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import us.ligusan.advent.advent2023.Point;

public class D21p2 {
    private static int SIZE_X;
    private static int SIZE_Y;

    public static void main(final String[] args) {
        List<List<Character>> data;

        final var steps = new HashSet<Point>();

        final var yRef = new AtomicInteger();
        try (var scanner = new Scanner(D21p2.class.getResourceAsStream("testInput.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                final var y = yRef.getAndIncrement();

                final var xRef = new AtomicInteger();
                return s.chars().mapToObj(i -> {
                    final var x = xRef.getAndIncrement();

                    final var ret = (char)i;
                    if (ret == 'S') {
                        steps.add(new Point(x, y));
                    }
                    return ret;
                }).collect(Collectors.toList());
            }).collect(Collectors.toList());
        }

        SIZE_Y = data.size();
        SIZE_X = data.get(0).size();

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

//        final var startingPoints = new HashMap<Point, Set<Point>>();
//        startingPoints.put(new Point(0, 0), steps);
//
//        final var sequences = new HashMap<Set<Point>, Map.Entry<Map<Set<Point>, Integer>, Optional<Integer>>>();
//        final var firstSequence = new HashMap<Set<Point>, Integer>();
//        firstSequence.put(steps, 0);
//        sequences.put(steps, Map.entry(firstSequence, Optional.empty()));
//
//        final var repeatingBlocks = new HashSet<Point>();
//
//        final var repeatingSequence = new HashMap<Set<Point>, Set<Point>>();
//        final var counterMap = new HashMap<Set<Point>, Integer>();

        Map<Point, Set<Point>> currentPosition = Collections.singletonMap(new Point(0,0), steps);
        int i = 0;
        for ( ; i < 200 ; ) {
            final var newPosition = currentPosition.stream().flatMap(point -> {
                final var x = point.x();
                final var y = point.y();
                return Stream.of(new Point(x + 1, y), new Point(x - 1, y), new Point(x, y + 1), new Point(x, y - 1))
                 .filter(newPoint -> {
                     final var normalizedNewPoint = normalize(newPoint);

                     final var c = data.get(normalizedNewPoint.y()).get(normalizedNewPoint.x());
                     return c == '.' || c == 'S';
                 });
            }).filter(point -> !repeatingBlocks.contains(findBlock(point))).collect(Collectors.toSet());

            System.out.format(
             "%d: currentPosition.size=%d, newPosition.size=%d, repeatingBlocks.size=%d, startingPoints.size=%d\n", i,
             currentPosition.size(), newPosition.size(), repeatingBlocks.size(), startingPoints.size());

            currentPosition = newPosition;
            i++;

            boolean foundNewFlag = false;
            for (final var blockEntry : newPosition.stream().collect(Collectors.groupingBy(point -> findBlock(point)))
             .entrySet()) {
                final var blockKey = blockEntry.getKey();
                final var normalizedBlock =
                 blockEntry.getValue().stream().map(point -> normalize(point)).collect(Collectors.toSet());

//                System.out.println(blockKey);
//                for (int y = 0 ; y < SIZE_Y ; y++) {
//                    for (int x = 0 ; x < SIZE_X ; x++) {
//                        System.out.format("%c", normalizedBlock.contains(new Point(x, y)) ? 'O' : data.get(y).get(x));
//                    }
//                    System.out.println();
//                }

                final var startingPoint = startingPoints.get(blockKey);
                if (startingPoint == null) {
                    if (startingPoints.containsValue(normalizedBlock)) {
                        System.out.println("repeating starting -> " + blockKey);
                        repeatingBlocks.add(blockKey);
                    } else {
                        startingPoints.put(blockKey, normalizedBlock);

                        final var newSequence = new HashMap<Set<Point>, Integer>();
                        newSequence.put(normalizedBlock, 0);
                        sequences.put(normalizedBlock, Map.entry(newSequence, Optional.empty()));

                        foundNewFlag = true;
                    }
                } else {
                    final var sequenceEntry = sequences.get(startingPoint);
                    final var sequenceMap = sequenceEntry.getKey();
                    final var existingIndex = sequenceMap.get(normalizedBlock);
                    if (existingIndex == null) {
                        sequenceMap.put(normalizedBlock, sequenceMap.size());

                        foundNewFlag = true;
                    } else {
                        sequences.put(startingPoint, Map.entry(sequenceMap, Optional.of(existingIndex)));

                        System.out.println("repeating existing -> " + blockKey);
                        repeatingBlocks.add(blockKey);
                    }
                }
            }
            if (!foundNewFlag) {
                break;
            }
        }

        for (final var sequenceEntry : sequences.entrySet()) {
            final var entry = sequenceEntry.getValue();
            final var sequenceMap = entry.getKey();
            final var size = sequenceMap.size();
            final var sequenceMapByIndex = sequenceMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            System.out.format("%s, %d, %d\n", sequenceEntry.getKey(), sequenceMapByIndex.get(size - 2).size(), sequenceMapByIndex.get(size - 1).size());
        }
    }

    private static Point normalize(final Point point) {
        var y = point.y();
        while (y < 0) {
            y += SIZE_Y;
        }
        while (y >= SIZE_Y) {
            y -= SIZE_Y;
        }

        var x = point.x();
        while (x < 0) {
            x += SIZE_X;
        }
        while (x >= SIZE_X) {
            x -= SIZE_X;
        }

        return new Point(x, y);
    }

    private static Point findBlock(final Point point) {
        int blockY = 0;
        var y = point.y();
        while (y < 0) {
            y += SIZE_Y;
            blockY--;
        }
        while (y >= SIZE_Y) {
            y -= SIZE_Y;
            blockY++;
        }

        int blockX = 0;
        var x = point.x();
        while (x < 0) {
            x += SIZE_X;
            blockX--;
        }
        while (x >= SIZE_X) {
            x -= SIZE_X;
            blockX++;
        }

        return new Point(blockX, blockY);
    }
}
