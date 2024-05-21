package us.ligusan.advent.advent2023.d22;

import us.ligusan.advent.advent2023.Point;
import us.ligusan.advent.advent2023.Point3;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D22p1 {
    static List<Map.Entry<Point3, Point3>> DATA;

    public static void main(final String[] args) {
        try (var scanner = new Scanner(D22p1.class.getResourceAsStream("input.txt"))) {
            DATA = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                final var matchResult = Pattern.compile("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)").matcher(s).results().findFirst().get();
                return Map.entry(new Point3(Integer.parseInt(matchResult.group(1)), Integer.parseInt(matchResult.group(2)), Integer.parseInt(matchResult.group(3))),
                        new Point3(Integer.parseInt(matchResult.group(4)), Integer.parseInt(matchResult.group(5)), Integer.parseInt(matchResult.group(6))));
            }).collect(Collectors.toList());
        }
        System.out.format("data=%s\n", DATA);

        final var size = DATA.size();

        DATA.sort(Comparator.comparingInt(entry -> Math.min(entry.getKey().z(), entry.getValue().z())));

        for (int i = 0; i < size; i++) {
            final var block = DATA.get(i);
            final var zDiff = Math.min(block.getKey().z(), block.getValue().z()) - ((i > 0 ? DATA.subList(0, i).stream().filter(entry -> intersect(getXY(block), getXY(entry))).mapToInt(entry -> Math.max(entry.getKey().z(), entry.getValue().z())).max().orElse(0) : 0) + 1);

            final var block1 = block.getKey();
            final var block2 = block.getValue();
            DATA.set(i, Map.entry(new Point3(block1.x(), block1.y(), block1.z() - zDiff), new Point3(block2.x(), block2.y(), block2.z() - zDiff)));
        }
        System.out.format("data=%s\n", DATA);

        final var onTop = new HashMap<Integer, List<Integer>>();
        final var supportedBy = new HashMap<Integer, List<Integer>>();

        for (int i = 0; i < size - 1; i++) {
            final var blockI = DATA.get(i);
            for (int j = i + 1; j < size; j++) {
                final var blockJ = DATA.get(j);
                if (Math.max(blockI.getKey().z(), blockI.getValue().z()) + 1 == Math.min(blockJ.getKey().z(), blockJ.getValue().z()) && intersect(getXY(blockI), getXY(blockJ))) {
                    onTop.computeIfAbsent(i, k -> new ArrayList<>()).add(j);
                    supportedBy.computeIfAbsent(j, k -> new ArrayList<>()).add(i);
                }
            }
        }

        var counter = size;
        for (final var onTopEntry : onTop.entrySet()) {
            if (onTopEntry.getValue().stream().anyMatch(onTopIndex -> supportedBy.get(onTopIndex).equals(Collections.singletonList(onTopEntry.getKey()))))
                counter--;
        }
        System.out.format("counter=%d\n", counter);
    }

    private static Map.Entry<Point, Point> getXY(final Map.Entry<Point3, Point3> entry) {
        final var key = entry.getKey();
        final var value = entry.getValue();
        return Map.entry(new Point(key.x(), key.y()), new Point(value.x(), value.y()));
    }

    private static boolean intersect(final Map.Entry<Point, Point> entry1, final Map.Entry<Point, Point> entry2) {
        final var x1 = Arrays.asList(entry1.getKey().x(), entry1.getValue().x());
        x1.sort(Integer::compareTo);
        final var x2 = Arrays.asList(entry2.getKey().x(), entry2.getValue().x());
        x2.sort(Integer::compareTo);
        final var y1 = Arrays.asList(entry1.getKey().y(), entry1.getValue().y());
        y1.sort(Integer::compareTo);
        final var y2 = Arrays.asList(entry2.getKey().y(), entry2.getValue().y());
        y2.sort(Integer::compareTo);

        return Math.max(x1.get(0), x2.get(0)) <= Math.min(x1.get(1), x2.get(1)) && Math.max(y1.get(0), y2.get(0)) <= Math.min(y1.get(1), y2.get(1));
    }
}

