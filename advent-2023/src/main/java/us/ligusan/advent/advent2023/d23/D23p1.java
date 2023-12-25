package us.ligusan.advent.advent2023.d23;

import us.ligusan.advent.advent2023.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D23p1 {
    public static void main(final String[] args) {
        List<List<Character>> data;

        try (var scanner = new Scanner(D23p1.class.getResourceAsStream("testInput.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> (char)i).collect(Collectors.toList())).collect(Collectors.toList());
        }

        final var sizeY = data.size();
        final var sizeX = data.get(0).size();

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        final var startPath = new ArrayList<Point>();
        startPath.add(new Point(1, 0));

        var maxLength = 0;

        final var paths = new ArrayList<List<Point>>();
        paths.add(startPath);
        while (!paths.isEmpty()) {
            final var path = paths.remove(0);
//            System.out.format("path=%s\n", path);

            final var lastPoint = path.get(path.size() - 1);
            final var x = lastPoint.x();
            final var y = lastPoint.y();
            if (x == sizeX - 2 && y == sizeY - 1) {
                maxLength = Math.max(maxLength, path.size());
                continue;
            }

            (switch (data.get(y).get(x)) {
                case '>' -> Stream.of(new Point(x + 1, y));
                case '<' -> Stream.of(new Point(x - 1, y));
                case '^' -> Stream.of(new Point(x, y - 1));
                case 'v' -> Stream.of(new Point(x, y + 1));
                default -> Stream.of(new Point(x - 1, y), new Point(x + 1, y), new Point(x, y - 1), new Point(x, y + 1));
            }).filter(point -> {
                final var newX = point.x();
                final var newY = point.y();
                return newX >= 0 && newX < sizeX && newY >= 0 && newY < sizeY;
            }).filter(point -> data.get(point.y()).get(point.x()) != '#').filter(point -> !path.contains(point)).map(point -> {
                final var newPath = new ArrayList<>(path);
                newPath.add(point);
                return newPath;
            }).forEach(paths::add);
        }

        System.out.println(maxLength - 1);
    }
}
