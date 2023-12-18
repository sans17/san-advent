package us.ligusan.advent.advent2023.d17;

import us.ligusan.advent.advent2023.Direction;
import us.ligusan.advent.advent2023.Point;

import java.util.*;
import java.util.stream.Collectors;

public class D17p1 {
    private static List<List<Integer>> DATA;

    public static void main(final String[] args) {
        try (var scanner = new Scanner(D17p1.class.getResourceAsStream("input.txt"))) {
            DATA = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> i - '0').collect(Collectors.toList())).collect(Collectors.toList());
        }

        final var sizeY = DATA.size();
        final var sizeX = DATA.get(0).size();

        DATA.forEach(list -> System.out.format("%s\n", list));

        final var bestPaths = new HashSet<List<Point>>();
        final var pathsMap = new TreeMap<Integer, List<List<Point>>>();
        final var startPath = List.of(new Point(sizeX - 1, sizeY - 1));
        pathsMap.put(DATA.get(sizeY - 1).get(sizeX - 1), new ArrayList<>(Arrays.asList(startPath)));

        for (; ; ) {
//            System.out.format("i=%d, paths.size=%d, bestPaths.size=%d\n", i, pathsMap.size(), bestPaths.size());

            final var pathsMapIterator = pathsMap.entrySet().iterator();
            final var scoreEntry = pathsMapIterator.next();
            pathsMapIterator.remove();

            final var pathScore = scoreEntry.getKey();
            final var paths = scoreEntry.getValue();
            System.out.format("pathScore=%d, paths.size=%s\n", pathScore, paths.size());
            for (final var path : paths) {
                if (bestPaths.contains(path)) continue;

//                System.out.format("\tpath=%s\n", path);
                final var pathXY = path.get(0);
                final var pathX = pathXY.x();
                final var pathY = pathXY.y();

                if (pathX == 0 && pathY == 0) {
                    System.out.println(path);
                    System.out.println(pathScore - DATA.get(0).get(0));

                    return;
                }
                bestPaths.add(path);

                var newPaths = Arrays.stream(Direction.values()).map(direction -> switch (direction) {
                    case UP -> new Point(pathX, pathY + 1);
                    case DOWN -> new Point(pathX, pathY - 1);
                    case LEFT -> new Point(pathX - 1, pathY);
                    case RIGHT -> new Point(pathX + 1, pathY);
                }).filter(point -> {
                    final var pointX = point.x();
                    final var pointY = point.y();
                    return pointX >= 0 && pointX < sizeX && pointY >= 0 && pointY < sizeY;
                }).filter(point -> path.size() < 2 || !point.equals(path.get(1))).map(point -> {
//                    System.out.format("point=%s\n", point);

                    final var newPath = new ArrayList<>(path);
                    newPath.add(0, point);
                    var i = 1;
                    for (; i < newPath.size(); i++) {
                        final var newPathJ = newPath.get(i);
                        if (point.x() != newPathJ.x() && point.y() != newPathJ.y())
                            break;
                    }
//                    System.out.format("i=%d, newPath=%s\n", i, newPath);
                    return newPath.subList(0, i);
                })
//                        .collect(Collectors.toList());
//                System.out.format("\tnewPaths=%s\n", newPaths);
//
//                newPaths = newPaths.stream()
                        .filter(newPath -> {
                    final var newPath0 = newPath.get(0);
                    return newPath.size() < (newPath0.x() == 0 && newPath0.y() == 0 ? 4 : 5);
                }).filter(newPath -> !bestPaths.contains(newPath)).collect(Collectors.toList());
                for (final var newPath : newPaths) {
                    final var newPath0 = newPath.get(0);
                    final var newPathScore = pathScore + DATA.get(newPath0.y()).get(newPath0.x());
//                    System.out.format("\tnewPathScore=%d, newPath=%s\n", newPathScore, newPath);
                    final var newPathScoreList = pathsMap.computeIfAbsent(newPathScore, k -> new ArrayList<>());
                    newPathScoreList.add(newPath);
                }
            }
        }
    }
}
