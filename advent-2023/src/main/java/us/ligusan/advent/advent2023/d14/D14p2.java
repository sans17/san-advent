package us.ligusan.advent.advent2023.d14;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D14p2 {
    public static void main(final String[] args) {
        final Set<Map.Entry<Integer, Integer>> squares = new HashSet<>();
        Set<Map.Entry<Integer, Integer>> rolls = new HashSet<>();

        final var sizeXRef = new AtomicInteger();
        final var sizeYRef = new AtomicInteger();

        try (var scanner = new Scanner(D14p2.class.getResourceAsStream("input.txt"))) {
            final var rollsFinal = rolls;
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                final var y = sizeYRef.getAndIncrement();

                final var xRef = new AtomicInteger();
                s.chars().forEach(i -> {
                    final var c = (char) i;
                    final var x = xRef.getAndIncrement();
                    if (c != '.') (c == 'O' ? rollsFinal : squares).add(Map.entry(x, y));
                });
                sizeXRef.set(xRef.get());
            });
        }
        final var sizeX = sizeXRef.get();
        final var sizeY = sizeYRef.get();

        System.out.format("squares=%s\n", squares);
        System.out.format("rolls=%s\n", rolls);

        final var CACHE = new HashMap<Set<Map.Entry<Integer, Integer>>, Integer>();

        final var limit = 1_000_000_000;
//        final var limit = 1;
        Integer cached = null;
        for (int i = 0; i < limit; i++) {
            System.out.format("%d, %d\n", i, rolls.stream().mapToInt(entry -> sizeX - entry.getValue()).reduce(Integer::sum).getAsInt());

            cached = CACHE.get(rolls);
            if (cached == null) CACHE.put(rolls, i);
            else {
                System.out.format("i=%d, cached=%d, CACHE.size=%d\n", i, cached, CACHE.size());
                break;
            }

            for (final var direction : Direction.values()) {
                rolls = rolls.stream().map(roll -> {
                    final var rollX = roll.getKey();
                    final var rollY = roll.getValue();
                    final var stoppingSquares = squares.stream().filter(square -> {
                        final var squareX = square.getKey();
                        final var squareY = square.getValue();
                        return switch (direction) {
                            case UP, DOWN -> squareX == rollX;
                            default -> squareY == rollY;
                        } && switch (direction) {
                            case UP -> squareY < rollY;
                            case DOWN -> squareY > rollY;
                            case LEFT -> squareX < rollX;
                            case RIGHT -> squareX > rollX;
                        };
                    });

                    final var ret = switch (direction) {
                        case UP, LEFT -> stoppingSquares.max(switch (direction) {
                            case UP -> Comparator.comparingInt(Map.Entry::getValue);
                            default -> Comparator.comparingInt(Map.Entry::getKey);
                        }).orElse(switch (direction) {
                            case UP -> Map.entry(rollX, -1);
                            default -> Map.entry(-1, rollY);
                        });
                        default -> stoppingSquares.min(switch (direction) {
                            case DOWN -> Comparator.comparingInt(Map.Entry::getValue);
                            default -> Comparator.comparingInt(Map.Entry::getKey);
                        }).orElse(switch (direction) {
                            case DOWN -> Map.entry(rollX, sizeY);
                            default -> Map.entry(sizeX, rollY);
                        });
                    };
                    return ret;
                }).collect(Collectors.groupingBy(square -> square, Collectors.counting())).entrySet().stream().flatMap(countingEntry -> Stream.iterate(0, j -> j < countingEntry.getValue(), j -> j + 1).map(j -> {
                    final var square = countingEntry.getKey();
                    return Map.entry(square.getKey() + (j + 1) * switch (direction) {
                        case LEFT -> 1;
                        case RIGHT -> -1;
                        default -> 0;
                    }, square.getValue() + (j + 1) * switch (direction) {
                        case UP -> 1;
                        case DOWN -> -1;
                        default -> 0;
                    });
                })).collect(Collectors.toSet());

//                final var rollsFinal = rolls;
//                Stream.iterate(0, y -> y < sizeY, y -> y + 1).forEach(y -> {
//                    Stream.iterate(0, x -> x < sizeX, x -> x + 1).forEach(x -> System.out.format("%c", squares.contains(Map.entry(x, y)) ? '#' : rollsFinal.contains(Map.entry(x, y)) ? 'O' : '.'));
//                    System.out.println();
//                });
//                System.out.format("rolls=%s\n", rolls);
//                System.out.format("\t%s, %d\n", direction, rolls.stream().mapToInt(entry -> sizeX - entry.getValue()).reduce(Integer::sum).getAsInt());
            }
        }

        final var cachedFinal = cached;
        final var rollsFinal = CACHE.entrySet().stream().filter(entry -> entry.getValue() == (limit - cachedFinal) % (CACHE.size() - cachedFinal) + cachedFinal).findFirst().get().getKey();
        Stream.iterate(0, y -> y < sizeY, y -> y + 1).forEach(y -> {
            Stream.iterate(0, x -> x < sizeX, x -> x + 1).forEach(x -> System.out.format("%c", squares.contains(Map.entry(x, y)) ? '#' : rollsFinal.contains(Map.entry(x, y)) ? 'O' : '.'));
            System.out.println();
        });

        rollsFinal.stream().mapToInt(entry -> sizeX - entry.getValue()).reduce(Integer::sum).ifPresent(System.out::println);
    }
}

enum Direction {
    UP, LEFT, DOWN, RIGHT
}
