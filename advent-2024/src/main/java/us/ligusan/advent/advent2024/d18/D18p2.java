package us.ligusan.advent.advent2024.d18;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D18p2 {
    public static void main(final String[] args) throws Exception {
        var end = 12;
        var sizeLimit = 6;
        var file = "testInput.txt";
//        var end = 1024;
//        var sizeLimit = 70;
//        var file = "input.txt";

        var data = Stream.iterate(0, i -> i <= sizeLimit, i -> i + 1).map(i -> new ArrayList<Integer>(Collections.nCopies(sizeLimit + 1, null))).collect(Collectors.toList());
        data.get(sizeLimit).set(sizeLimit, 0);
        var start = Map.entry(0, 0);

        try (var scanner = new Scanner(D18p2.class.getResourceAsStream(file))) {
            var j = 0;
            for (; j < end; j++) {
                var s = scanner.nextLine();
                System.out.println(s);
                var m = Pattern.compile("(\\d+),(\\d+)").matcher(s);
                m.find();
                data.get(Integer.parseInt(m.group(2))).set(Integer.parseInt(m.group(1)), -1);
            }

            var i = 0;
            for (var edge = Set.of(Map.entry(sizeLimit, sizeLimit)); !edge.contains(start); i++) {
                System.out.format("i=%d%n", i);
                edge = edge.stream().flatMap(e -> {
                    var x = e.getKey();
                    var y = e.getValue();
                    return Stream.of(Map.entry(x - 1, y), Map.entry(x + 1, y), Map.entry(x, y - 1), Map.entry(x, y + 1));
                }).filter(e -> {
                    var x = e.getKey();
                    var y = e.getValue();
                    return x >= 0 && x <= sizeLimit && y >= 0 && y <= sizeLimit;
                }).filter(e -> data.get(e.getValue()).get(e.getKey()) == null).collect(Collectors.toSet());

                edge.stream().collect(Collectors.toMap(e -> e, e -> {
                    var x = e.getKey();
                    var y = e.getValue();
                    return Stream.of(Map.entry(x - 1, y), Map.entry(x + 1, y), Map.entry(x, y - 1), Map.entry(x, y + 1)).filter(e1 -> {
                        var x1 = e1.getKey();
                        var y1 = e1.getValue();
                        return x1 >= 0 && x1 <= sizeLimit && y1 >= 0 && y1 <= sizeLimit;
                    }).map(e1 -> data.get(e1.getValue()).get(e1.getKey())).filter(v -> v != null && v >= 0).min(Integer::compareTo).get();
                })).forEach((e, v) -> data.get(e.getValue()).set(e.getKey(), v + 1));

                for (var l : data) System.out.println(l);
            }

            String s = null;
            for (; !data.getFirst().getFirst().equals(-1); j++) {
                System.out.format("j=%d%n", j);

                s = scanner.nextLine();
                System.out.println(s);
                var m = Pattern.compile("(\\d+),(\\d+)").matcher(s);
                m.find();

                var xj = Integer.parseInt(m.group(1));
                var yj = Integer.parseInt(m.group(2));
                var edgeValue = data.get(yj).get(xj);
                data.get(yj).set(xj, -1);
                if (edgeValue != null && edgeValue >= 0) {
                    for (var edge = Set.of(Map.entry(xj, yj)); !edge.isEmpty(); ) {
                        final var edgeValueFinal = edgeValue;
                        final var edgeValueFinal1 = ++edgeValue;

                        edge = edge.stream().flatMap(e -> {
                            var x = e.getKey();
                            var y = e.getValue();
                            return Stream.of(Map.entry(x - 1, y), Map.entry(x + 1, y), Map.entry(x, y - 1), Map.entry(x, y + 1));
                        }).filter(e -> {
                            var x = e.getKey();
                            var y = e.getValue();
                            return x >= 0 && x <= sizeLimit && y >= 0 && y <= sizeLimit;
                        }).filter(e -> edgeValueFinal1.equals(data.get(e.getValue()).get(e.getKey()))).collect(Collectors.toSet());

                        System.out.format("edgeValue=%d, edge=%s%n", edgeValue, edge);

                        edge.stream().filter(e -> {
                            var x = e.getKey();
                            var y = e.getValue();
                            return Stream.of(Map.entry(x - 1, y), Map.entry(x + 1, y), Map.entry(x, y - 1), Map.entry(x, y + 1)).filter(e1 -> {
                                var x1 = e1.getKey();
                                var y1 = e1.getValue();
                                return x1 >= 0 && x1 <= sizeLimit && y1 >= 0 && y1 <= sizeLimit;
                            }).map(e1 -> data.get(e1.getValue()).get(e1.getKey())).noneMatch(v -> edgeValueFinal.equals(v));
                        }).forEach((e) -> data.get(e.getValue()).set(e.getKey(), -1));

                        if (data.getFirst().getFirst().equals(-1)) break;
                    }

                    for (var l : data) System.out.println(l);
                }
            }
            System.out.println(s);
        }
    }
}
