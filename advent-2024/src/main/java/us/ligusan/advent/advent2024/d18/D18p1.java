package us.ligusan.advent.advent2024.d18;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D18p1 {
    public static void main(final String[] args) throws Exception {
//        var end = 12;
//        var sizeLimit = 6;
//        var file = "testInput.txt";
        var end = 1024;
        var sizeLimit = 70;
        var file = "input.txt";

        var data = Stream.iterate(0, i -> i <= sizeLimit, i -> i + 1).map(_ -> new ArrayList<Integer>(Collections.nCopies(sizeLimit + 1, null))).collect(Collectors.toList());

        try (var scanner = new Scanner(D18p1.class.getResourceAsStream(file))) {
            for (var i = 0; i < end; i++) {
                var s = scanner.nextLine();
                System.out.println(s);
                var m = Pattern.compile("(\\d+),(\\d+)").matcher(s);
                m.find();
                data.get(Integer.parseInt(m.group(2))).set(Integer.parseInt(m.group(1)), -1);
            }
        }
        data.get(sizeLimit).set(sizeLimit, 0);
        for (var l : data) System.out.println(l);

        var start = Map.entry(0, 0);
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
            System.out.format("edge=%s%n", edge);

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
        System.out.println(data.get(start.getValue()).get(start.getKey()));
    }
}
