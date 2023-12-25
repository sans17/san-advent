package us.ligusan.advent.advent2023.d23;

import us.ligusan.advent.advent2023.Point;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D23p2 {
    static private final Set<Point> VERTICES = new HashSet<>();
    static final Set<List<Point>> EDGES = new HashSet<>();

    static Point END_POINT;

    public static void main(final String[] args) {
        List<List<Character>> data;

        try (var scanner = new Scanner(D23p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList());
        }

        final var sizeY = data.size();
        final var sizeX = data.get(0).size();

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        final var startPoint = new Point(1, 0);
        END_POINT = new Point(sizeX - 2, sizeY - 1);
        System.out.format("startPoint=%s, endPoint=%s\n", startPoint, END_POINT);

        VERTICES.add(startPoint);

        final var startPath = new ArrayList<Point>();
        startPath.add(startPoint);
        startPath.add(new Point(1, 1));

        final var paths = new ArrayList<List<Point>>();
        paths.add(startPath);
        while (!paths.isEmpty()) {
//            System.out.format("paths.size=%d\n", paths.size());
            final var path = paths.remove(0);
//            System.out.format("path=%s\n", path);

            final var pathStartEnd = new HashSet<Point>();
            pathStartEnd.add(path.get(0));
            pathStartEnd.add(path.get(path.size() - 1));
            if (EDGES.stream().map(edge -> {
                final var startEnd = new HashSet<Point>();
                startEnd.add(edge.get(0));
                startEnd.add(edge.get(edge.size() - 1));
                return startEnd;
            }).collect(Collectors.toList()).contains(pathStartEnd)) continue;

            final var lastPoint = path.get(path.size() - 1);
            final var x = lastPoint.x();
            final var y = lastPoint.y();
            if (lastPoint.equals(END_POINT)) {
                VERTICES.add(lastPoint);
                EDGES.add(path);
                continue;
            }

            final var newPoints = Stream.of(new Point(x - 1, y), new Point(x + 1, y), new Point(x, y - 1), new Point(x, y + 1)).filter(point -> {
                final var newX = point.x();
                final var newY = point.y();
                return newX >= 0 && newX < sizeX && newY >= 0 && newY < sizeY;
            }).filter(point -> data.get(point.y()).get(point.x()) != '#').filter(point -> !path.contains(point)).collect(Collectors.toList());
            if (newPoints.size() == 1) {
                path.add(newPoints.get(0));
                paths.add(path);
            } else {
                VERTICES.add(lastPoint);
                EDGES.add(path);

                newPoints.stream().forEach(point -> {
                    final var newPath = new ArrayList<Point>();
                    newPath.add(lastPoint);
                    newPath.add(point);
                    paths.add(newPath);
                });
            }
        }
        System.out.format("VERTICES=%s\n", VERTICES);
        EDGES.forEach(edge -> System.out.format("%s - %s, %d; ", edge.get(0), edge.get(edge.size() - 1), edge.size()));
        System.out.println();

        System.out.println(maxPath(startPoint, Collections.singleton(startPoint)));
    }

    final static Map<Point, Map<Set<Point>, Boolean>> PATH_EXISTS_MAP = new HashMap<>();
    final static Map<Point, Map<Set<Point>, OptionalInt>> OPTIONAL_MAX_PATHS_MAP = new HashMap<>();

    private static Point getOtherNode(final List<Point> edge, final Point point) {
        final var fistNode = edge.get(0);
        return fistNode.equals(point) ? edge.get(edge.size() - 1) : fistNode;
    }

    private static Stream<List<Point>> nextStep(final Point point, final Set<Point> exclude) {
        return EDGES.stream().filter(edge -> edge.get(0).equals(point) || edge.get(edge.size() - 1).equals(point)).filter(edge -> !exclude.contains(getOtherNode(edge, point)));
    }

    private static OptionalInt maxPath(final Point start, final Set<Point> exclude) {
        System.out.format("start=%s, exclude=%s\n", start, exclude);

        if (start.equals(END_POINT)) return OptionalInt.of(0);

        final var maxPaths = OPTIONAL_MAX_PATHS_MAP.computeIfAbsent(start, key -> new HashMap<>());
        final var optionalMaxPath = maxPaths.get(exclude);
        if (optionalMaxPath != null) return optionalMaxPath;

        return nextStep(start, exclude).mapMultiToInt((nextStepEdge, consumer) -> {
            final var nextNode = getOtherNode(nextStepEdge, start);

            final var newExclude = new HashSet<>(exclude);
            newExclude.add(nextNode);

            final var pathExist = PATH_EXISTS_MAP.computeIfAbsent(nextNode, key -> new HashMap<>());
            var pathExistsFlag = pathExist.get(newExclude);
            if (pathExistsFlag == null) {
                final var paths = new ArrayList<List<Point>>();
                paths.add(new ArrayList<>(List.of(nextNode)));
                boolean exists = false;
                for (; !paths.isEmpty(); ) {
                    final var path = paths.remove(0);
//                    System.out.format("\tpath=%s\n", path);

                    final var lastPoint = path.get(path.size() - 1);
                    if (lastPoint.equals(END_POINT)) {
                        exists = true;
                        break;
                    }

                    nextStep(lastPoint, newExclude).forEach(edge -> {
                        final var otherNode = getOtherNode(edge, lastPoint);
                        if (path.contains(otherNode)) return;

                        final var newPath = new ArrayList<>(path);
                        newPath.add(otherNode);
                        paths.add(0, newPath);
                    });
                }
                pathExist.put(newExclude, pathExistsFlag = exists);
            }
            if (!pathExistsFlag) return;

            final var nextMax = maxPath(nextNode, newExclude);
            System.out.format("nextNode=%s, newExclude=%s, nextMax=%s\n", nextNode, newExclude, nextMax);
            OPTIONAL_MAX_PATHS_MAP.computeIfAbsent(nextNode, key -> new HashMap<>()).put(newExclude, nextMax);

            if (nextMax.isPresent())
                consumer.accept(nextMax.getAsInt() + nextStepEdge.size() - 1);
        }).max();
    }
}
