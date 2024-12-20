package us.ligusan.advent.advent2024.d20;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class D20p2 {
    public static void main(final String[] args) throws Exception {
//        var file = "testInput.txt";
//        var limit = 74;
        var file = "input.txt";
        final var limit = 100;
        final var chit = 20;

        final List<List<Character>> data;

        final var xIndex = new AtomicInteger();
        final var yIndex = new AtomicInteger();

        final var xStart = new AtomicInteger();
        final var yStart = new AtomicInteger();
        final var xEnd = new AtomicInteger();
        final var yEnd = new AtomicInteger();

        try (var scanner = new Scanner(D20p2.class.getResourceAsStream(file))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                xIndex.set(0);

                final var list = s.chars().mapToObj(i -> {
                    final var c = (char) i;

                    boolean startFlag = c == 'S';
                    if (startFlag || c == 'E') {
                        (startFlag ? xStart : xEnd).set(xIndex.get());
                        (startFlag ? yStart : yEnd).set(yIndex.get());
                    }

                    xIndex.getAndIncrement();
                    return c;
                }).toList();
                yIndex.incrementAndGet();

                return list;
            }).toList();
        }
        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });
        System.out.format("xStart=%s, yStart=%s, xEnd=%s, yEnd=%s%n", xStart, yStart, xEnd, yEnd);

        final var xSize = xIndex.get();
        final var ySize = yIndex.get();

        var end = Map.entry(xEnd.get(), yEnd.get());

        var distanceMap = new HashMap<Map.Entry<Integer, Integer>, Integer>();
        distanceMap.put(end, 0);

        for (var edge = end; !edge.equals(Map.entry(xStart.get(), yStart.get())); ) {
            var distance = distanceMap.get(edge);

            var xEdge = edge.getKey();
            var yEdge = edge.getValue();
            edge = Stream.of(Map.entry(xEdge - 1, yEdge), Map.entry(xEdge + 1, yEdge), Map.entry(xEdge, yEdge - 1), Map.entry(xEdge, yEdge + 1)).filter(e -> data.get(e.getValue()).get(e.getKey()) != '#' && !distanceMap.containsKey(e)).findFirst().get();
//            System.out.format("edge=%s%n", edge);

            distanceMap.put(edge, distance + 1);
        }
//        for (int i = 0; i < ySize; i++) {
//            for (int j = 0; j < xSize; j++) {
//                var e = Map.entry(j, i);
//                var c = data.get(i).get(j);
//                var d = distanceMap.get(e);
//                System.out.format("%c", c != '.' ? c : d == null ? '.' : Integer.toString(d % 36, 36).charAt(0));
//            }
//            System.out.println();
//        }

        System.out.println(distanceMap.entrySet().stream().mapToLong(e -> {
            var location = e.getKey();
            var eDistance = e.getValue();

            var x = location.getKey();
            var y = location.getValue();
            var ret = distanceMap.entrySet().stream().filter(e1 -> {
                var location1 = e1.getKey();
                var diff = Math.abs(location1.getKey() - x) + Math.abs(location1.getValue() - y);

                var distance = e1.getValue();

                var retFlag = diff <= chit && eDistance - distance - diff >= limit;
//                if (retFlag) System.out.format("e1=%s, distance=%s%n", e1, distance);
                return retFlag;
            }).count();
//            if (ret > 0) System.out.format("location=%s, eDistance=%s, ret=%s%n", location, eDistance, ret);
            return ret;
        }).sum());
    }
}
