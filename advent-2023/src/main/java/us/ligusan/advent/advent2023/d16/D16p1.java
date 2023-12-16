package us.ligusan.advent.advent2023.d16;

import us.ligusan.advent.advent2023.Direction;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D16p1 {
    public static void main(final String[] args) {
        List<List<Character>> data;

        try (var scanner = new Scanner(D16p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList());
        }

        final var sizeY = data.size();
        final var sizeX = data.get(0).size();

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        final var lights = new HashSet<Map.Entry<Map.Entry<Integer, Integer>, Direction>>();
        var newLights = Collections.singleton(Map.entry(Map.entry(-1, 0), Direction.RIGHT));

        for (; ; ) {
            newLights = newLights.stream().<Map.Entry<Map.Entry<Integer, Integer>, Direction>>mapMulti((light, consumer) -> {
                final var lightXY = light.getKey();
                final var lightDirection = light.getValue();

                final var newX = lightXY.getKey() + switch (lightDirection) {
                    case LEFT -> -1;
                    case RIGHT -> 1;
                    default -> 0;
                };
                final var newY = lightXY.getValue() + switch (lightDirection) {
                    case UP -> -1;
                    case DOWN -> 1;
                    default -> 0;
                };
                if (newX < 0 || newX >= sizeX || newY < 0 || newY >= sizeY) return;

                for (final var newDirection : switch (data.get(newY).get(newX)) {
                    case '/' -> Collections.singleton(switch (lightDirection) {
                        case UP -> Direction.RIGHT;
                        case DOWN -> Direction.LEFT;
                        case LEFT -> Direction.DOWN;
                        case RIGHT -> Direction.UP;
                    });
                    case '\\' -> Collections.singleton(switch (lightDirection) {
                        case UP -> Direction.LEFT;
                        case DOWN -> Direction.RIGHT;
                        case LEFT -> Direction.UP;
                        case RIGHT -> Direction.DOWN;
                    });
                    case '-' -> switch (lightDirection) {
                        case UP, DOWN -> Arrays.asList(Direction.LEFT, Direction.RIGHT);
                        default -> Collections.singleton(lightDirection);
                    };
                    case '|' -> switch (lightDirection) {
                        case LEFT, RIGHT -> Arrays.asList(Direction.UP, Direction.DOWN);
                        default -> Collections.singleton(lightDirection);
                    };
                    default -> Collections.singleton(lightDirection);
                })
                    consumer.accept(Map.entry(Map.entry(newX, newY), newDirection));
            }).collect(Collectors.toSet());
            newLights.removeAll(lights);
            if (newLights.isEmpty()) break;
            System.out.format("newLights=%s\n", newLights);

            lights.addAll(newLights);

//            Stream.iterate(0, y -> y < sizeY, y -> y + 1).forEach(y -> {
//                Stream.iterate(0, x -> x < sizeX, x -> x + 1).forEach(x -> lights.stream().filter(light -> {
//                    final var lightXY = light.getKey();
//                    return lightXY.getKey() == x && lightXY.getValue() == y;
//                }).findAny().ifPresentOrElse(light -> System.out.print('#'), () -> System.out.print(data.get(y).get(x))));
//                System.out.println();
//            });
        }


        System.out.format("%d\n", lights.stream().map(Map.Entry::getKey).collect(Collectors.toSet()).size());
    }
}
