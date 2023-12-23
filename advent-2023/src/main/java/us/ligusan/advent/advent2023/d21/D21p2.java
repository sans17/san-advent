package us.ligusan.advent.advent2023.d21;

import java.math.BigInteger;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import us.ligusan.advent.advent2023.Point;

public class D21p2 {
    public static void main(final String[] args) {
        List<List<Character>> data;

        final var startRef = new AtomicReference<Point>();

        final var yRef = new AtomicInteger();
        try (var scanner = new Scanner(D21p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                final var y = yRef.getAndIncrement();

                final var xRef = new AtomicInteger();
                return s.chars().mapToObj(i -> {
                    final var x = xRef.getAndIncrement();

                    final var ret = (char) i;
                    if (ret == 'S') {
                        startRef.set(new Point(x, y));
                    }
                    return ret;
                }).collect(Collectors.toList());
            }).collect(Collectors.toList());
        }

        final var sizeX = data.size();
        final var sizeY = data.get(0).size();

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        final var statesList = new ArrayList<Set<Point>>();
        statesList.add(Collections.singleton(startRef.get()));
        final var statesMap = new HashMap<Integer, Set<Point>>();
        statesMap.put(0, statesList.get(0));

        final var rules = new HashMap<Map<Point, Integer>, Map<Point, Integer>>();

        var currentPosition = Collections.singletonMap(new Point(0, 0), 0);
        int k = 0;
        for (; k < 1000; k++) {
//            System.out.format("k=%d\n", k);
//            System.out.format("%d: %s\n", k, currentPosition);
            System.out.format("%d: currentPosition.size=%d, statesMap.size=%d\n", k, currentPosition.size(), statesMap.size());
//            System.out.format("statesMap=%s\n", statesMap);

            final var currentPositionFinal = currentPosition;

            final var newPosition = new HashMap<Point, Integer>();

            var minX = 0;
            var maxX = 0;
            var minY = 0;
            var maxY = 0;
            for (final var blockPoint : currentPosition.keySet()) {
                minX = Math.min(minX, blockPoint.x());
                maxX = Math.max(maxX, blockPoint.x());
                minY = Math.min(minY, blockPoint.y());
                maxY = Math.max(maxY, blockPoint.y());
            }

//            for (int y = minY; y <= maxY; y++)
//                for (int i = 0; i < sizeY; i++) {
//                    for (int x = minX; x <= maxX; x++) {
//                        final var steps = statesMap.get(currentPosition.get(new Point(x, y)));
//
//                        for (int j = 0; j < sizeX; j++)
//                            System.out.format("%c", steps == null || !steps.contains(new Point(j, i)) ? data.get(i).get(j) : 'O');
//                    }
//                    System.out.println();
//                }

            boolean breakFlag = false;

            for (int x = minX; x <= maxX; x++)
                for (int y = minY; y <= maxY; y++) {
                    final var currentValue = currentPosition.get(new Point(x, y));

                    if (currentValue == null) continue;

                    final var xFinal = x;
                    final var yFinal = y;

                    final var ruleKey = Stream.of(new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(0, -1), new Point(0, 0)).map(delta -> Map.entry(delta, new Point(xFinal + delta.x(), yFinal + delta.y()))).filter(deltaEntry -> currentPositionFinal.containsKey(deltaEntry.getValue())).collect(Collectors.toMap(deltaEntry -> deltaEntry.getKey(), deltaEntry -> currentPositionFinal.get(deltaEntry.getValue())));
                    var ruleValue = rules.get(ruleKey);

                    if (ruleValue == null) {
                        ruleValue = new HashMap<>();

                        final var neighbours = ruleKey.entrySet().stream().flatMap(deltaEntry -> {
                            final var delta = deltaEntry.getKey();
                            return statesMap.get(deltaEntry.getValue()).stream().map(stepPoint -> new Point(stepPoint.x() + sizeX * delta.x(), stepPoint.y() + sizeY * delta.y()));
                        }).flatMap(stepPoint -> {
                            final var stepX = stepPoint.x();
                            final var stepY = stepPoint.y();
                            return Stream.of(new Point(stepX + 1, stepY), new Point(stepX - 1, stepY), new Point(stepX, stepY + 1), new Point(stepX, stepY - 1));
                        }).filter(stepPoint -> {
                            final var stepX = stepPoint.x();
                            final var stepY = stepPoint.y();
                            return stepX >= -1 && stepX <= sizeX && stepY > -1 && stepY < sizeY || stepX > -1 && stepX < sizeX && stepY >= -1 && stepY <= sizeY;
                        }).filter(stepPoint -> {
                            final var c = data.get((stepPoint.y() + sizeY) % sizeY).get((stepPoint.x() + sizeX) % sizeX);
                            return c == '.' || c == 'S';
                        }).collect(Collectors.toSet());

                        for (final var deltaStepsEntry : neighbours.stream().collect(Collectors.groupingBy(stepPoint -> {
                            final var stepX = stepPoint.x();
                            if (stepX < 0) return new Point(-1, 0);
                            if (stepX >= sizeX) return new Point(1, 0);
                            final var stepY = stepPoint.y();
                            if (stepY < 0) return new Point(0, -1);
                            if (stepY >= sizeY) return new Point(0, 1);
                            return new Point(0, 0);
                        })).entrySet()) {
                            final var delta = deltaStepsEntry.getKey();
                            final var deltaX = delta.x();
                            final var deltaY = delta.y();
                            if ((deltaX != 0 || deltaY != 0) && currentPosition.containsKey(new Point(x + deltaX, y + deltaY)))
                                continue;

                            final var steps = deltaStepsEntry.getValue().stream().map(stepPoint -> new Point((stepPoint.x() + sizeX) % sizeX, (stepPoint.y() + sizeY) % sizeY)).collect(Collectors.toSet());
                            var stepsIndex = statesList.indexOf(steps);
                            if (stepsIndex < 0) {
                                stepsIndex = statesList.size();
                                statesList.add(steps);
                                statesMap.put(stepsIndex, steps);
                            }
                            ruleValue.put(delta, stepsIndex);
                        }

                        rules.put(ruleKey, ruleValue);
                    }

                    for (final var deltaEntry : ruleValue.entrySet()) {
                        final var delta = deltaEntry.getKey();

                        newPosition.put(new Point(x + delta.x(), y + delta.y()), deltaEntry.getValue());
                    }

                    if (y == -3 && statesMap.get(ruleValue.get(new Point(0, 0))).stream().map(stepPoint -> stepPoint.y()).anyMatch(stepY -> stepY == 0))
                        breakFlag = true;
                }

            currentPosition = newPosition;
            if (breakFlag) break;
        }

        System.out.format("k=%d\n", k);
//        System.out.format("currentPosition=%s\n", currentPosition);
        System.out.format("currentPosition.size=%d\n", currentPosition.size());
//        System.out.format("statesMap=%s\n", statesMap);
        System.out.format("statesMap.size=%d\n", statesMap.size());
        System.out.println(currentPosition.values().stream().collect(Collectors.groupingBy(key -> key, Collectors.counting())));
        currentPosition.values().stream().collect(Collectors.groupingBy(key -> key, Collectors.counting())).entrySet().stream().map(entry -> BigInteger.valueOf(statesMap.get(entry.getKey()).size()).multiply(BigInteger.valueOf(entry.getValue()))).reduce(BigInteger::add).ifPresent(System.out::println);

        var minX = 0;
        var maxX = 0;
        var minY = 0;
        var maxY = 0;
        for (final var blockPoint : currentPosition.keySet()) {
            minX = Math.min(minX, blockPoint.x());
            maxX = Math.max(maxX, blockPoint.x());
            minY = Math.min(minY, blockPoint.y());
            maxY = Math.max(maxY, blockPoint.y());
        }

//        for (int y = minY; y <= maxY; y++)
//            for (int i = 0; i < sizeY; i++) {
//                for (int x = minX; x <= maxX; x++) {
//                    final var steps = statesMap.get(currentPosition.get(new Point(x, y)));
//
//                    for (int j = 0; j < sizeX; j++)
//                        System.out.format("%c", steps == null || !steps.contains(new Point(j, i)) ? data.get(i).get(j) : 'O');
//                }
//                System.out.println();
//            }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                final var state = currentPosition.get(new Point(x, y));
                if (state == null)
                    System.out.print("     ");
                else
                    System.out.format("%5d", state);
            }
            System.out.println();
        }

        var numberOfCycles = BigInteger.valueOf((26501365 - 65) / 131);
        var numberOfCyclesMinusOne = numberOfCycles.add(BigInteger.valueOf(-1));
        System.out.format("numberOfCycles=%d\n", numberOfCycles);

        var ret = BigInteger.valueOf(statesMap.get(388).size()).multiply(numberOfCycles).multiply(numberOfCycles);
        ret = ret.add(BigInteger.valueOf(statesMap.get(383).size()).multiply(numberOfCyclesMinusOne).multiply(numberOfCyclesMinusOne));
        ret = ret.add(Stream.of(907, 909, 912, 914).map(state -> statesMap.get(state).size()).map(BigInteger::valueOf).reduce(BigInteger::add).get().multiply(numberOfCycles));
        ret = ret.add(Stream.of(1687, 1688, 1689, 1690).map(state -> statesMap.get(state).size()).map(BigInteger::valueOf).reduce(BigInteger::add).get().multiply(numberOfCyclesMinusOne));
        ret = ret.add(Stream.of(908, 910, 911, 913).map(state -> statesMap.get(state).size()).map(BigInteger::valueOf).reduce(BigInteger::add).get());

        System.out.println(ret);
    }
}
