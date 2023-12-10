package us.ligusan.advent.advent2023.d10;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D10p2 {
    private static List<List<Character>> DATA;

    private static boolean inside(final Map.Entry<Integer, Integer> xy) {
        final var x = xy.getKey();
        final var y = xy.getValue();
        return x >= 0 && x < DATA.get(0).size() && y >= 0 && y < DATA.size();
    }

    public static void main(final String[] args) {

        final var xIndex = new AtomicInteger();
        final var yIndex = new AtomicInteger();

        final var xStart = new AtomicInteger();
        final var yStart = new AtomicInteger();

        try (var scanner = new Scanner(D10p2.class.getResourceAsStream("input.txt"))) {
            DATA = scanner.useDelimiter("\r?\n").tokens().map(s -> {
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

        DATA.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });
        System.out.format("xStart=%d, yStart=%d\n", xStart.get(), yStart.get());

        final List<List<Character>> data2 = Stream.iterate(0, y -> y < DATA.size(), y -> y + 1).map(y -> Stream.iterate(0, x -> x < DATA.get(0).size(), x -> x + 1).map(x -> {
            if (DATA.get(y).get(x) == '.') {
                return '+';
            } else return ' ';
        }).collect(Collectors.toList())).collect(Collectors.toList());

        final var path = new HashSet<Map.Entry<Integer, Integer>>();
        var x = xStart.get();
        var y = yStart.get();
        var currentPosition = Map.entry(x, y);
        var currentDirection = Map.entry(0, -1);
        var nextC = '|';
//        var currentDirection = Map.entry(1, 0);
//        var nextC = 'F';
//        var currentDirection = Map.entry(0, 1);
//        var nextC = '7';

        Set<Map.Entry<Integer, Integer>> right = new HashSet<>();

        while (true) {
//            System.out.format("\tcurrentPosition=%s, currentDirection=%s, nextC=%c\n", currentPosition, currentDirection, nextC);
            path.add(currentPosition);

            x = currentPosition.getKey();
            y = currentPosition.getValue();

            if (currentDirection.equals(Map.entry(0, 1))) switch (nextC) {
                case '|':
//                    System.out.format("\tcurrentPosition=%s, currentDirection=%s\n", currentPosition, currentDirection);
                    right.add(Map.entry(x - 1, y));
//                    System.out.format("\tright=%s\n", right);
                    break;
                case '7':
                    right.add(Map.entry(x - 1, y + 1));
                    break;

                // F
                default:
                    right.add(Map.entry(x, y - 1));
                    right.add(Map.entry(x - 1, y - 1));
                    right.add(Map.entry(x - 1, y));
                    break;

            } else if (currentDirection.equals(Map.entry(0, -1))) switch (nextC) {
                case '|':
                    right.add(Map.entry(x + 1, y));
                    break;

                case 'L':
                    right.add(Map.entry(x + 1, y - 1));
                    break;

                // J
                default:
                    right.add(Map.entry(x, y + 1));
                    right.add(Map.entry(x + 1, y + 1));
                    right.add(Map.entry(x + 1, y));
                    break;
            } else if (currentDirection.equals(Map.entry(1, 0))) switch (nextC) {
                case '-':
                    right.add(Map.entry(x, y + 1));
                    break;

                case 'L':
                    right.add(Map.entry(x-1, y));
                    right.add(Map.entry(x-1, y + 1));
                    right.add(Map.entry(x, y + 1));
                    break;

                    // F
                    default:
                        right.add(Map.entry(x+1, y+1));
                        break;
            } else switch (nextC) {
                case '-':
                    right.add(Map.entry(x, y - 1));
                    break;

                case '7':
                    right.add(Map.entry(x + 1, y));
                    right.add(Map.entry(x + 1, y - 1));
                    right.add(Map.entry(x, y - 1));
                    break;

                // J
                default:
                    right.add(Map.entry(x - 1, y - 1));
                    break;
            }
//            System.out.format("\tright=%s\n", right);

            data2.get(y).set(x, '.');

            final var nextX = x + currentDirection.getKey();
            final var nextY = y + currentDirection.getValue();
            if (!inside(Map.entry(nextX, nextY))) break;

            nextC = DATA.get(nextY).get(nextX);
//            System.out.format("\tnextC=%c\n", nextC);

            if (nextC == 'S') break;

            currentPosition = Map.entry(nextX, nextY);
            currentDirection = currentDirection.equals(Map.entry(0, 1)) ? switch (nextC) {
                case '|' -> Map.entry(0, 1);
                case 'L' -> Map.entry(1, 0);
                // J
                default -> Map.entry(-1, 0);
            } : currentDirection.equals(Map.entry(0, -1)) ? switch (nextC) {
                case '|' -> Map.entry(0, -1);
                case '7' -> Map.entry(-1, 0);
                // F
                default -> Map.entry(1, 0);
            } : currentDirection.equals(Map.entry(1, 0)) ? switch (nextC) {
                case '-' -> Map.entry(1, 0);
                case '7' -> Map.entry(0, 1);
                // J
                default -> Map.entry(0, -1);
            } : switch (nextC) {
                case '-' -> Map.entry(-1, 0);
                case 'L' -> Map.entry(0, -1);
                default -> Map.entry(0, 1);
            };
        }
//        System.out.format("%d\n", path.length()/2);

        right.removeAll(path);
        System.out.format("right=%s\n", right);

        right = Stream.iterate(Map.entry(new HashSet<Map.Entry<Integer, Integer>>(), right), setEntry -> !setEntry.getValue().isEmpty(),
                setEntry -> {
                    final var oldSet = setEntry.getKey();
                    final var additionalSet = setEntry.getValue();
                    System.out.format("\toldSet.size=%d, oldSet=%s\n\tadditionalSet.size=%d, additionalSet=%s\n", oldSet.size(), oldSet, additionalSet.size(), additionalSet);

                    oldSet.addAll(additionalSet);
                    additionalSet.clear();

                    oldSet.stream().forEach(entry -> {
                        final var sideX = entry.getKey();
                        final var sideY = entry.getValue();

                        additionalSet.addAll(Arrays.asList(Map.entry(sideX + 1, sideY), Map.entry(sideX - 1, sideY), Map.entry(sideX, sideY + 1), Map.entry(sideX, sideY - 1)).stream().filter(newEntry -> inside(newEntry)).collect(Collectors.toList()));
                    });
                    additionalSet.removeAll(path);
                    additionalSet.removeAll(oldSet);

                    return Map.entry(oldSet, additionalSet);
                }).reduce((a, b) -> b).get().getKey().stream().collect(Collectors.toSet());

        right.stream().forEach(entry -> data2.get(entry.getValue()).set(entry.getKey(), 'X'));
        data2.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        System.out.format("right.size=%s\n", right.size());
    }
}
