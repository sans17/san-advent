package us.ligusan.advent.advent2024.d14;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D14p1 {
    public static void main(final String[] args) throws Exception {
//        var xSize = 11;
//        var ySize = 7;
//        var file = "testInput.txt";
        var xSize = 101;
        var ySize = 103;
        var file = "input.txt";

        var xHalf = (xSize - 1) / 2;
        var yHalf = (ySize - 1) / 2;

        var t = 100;

        try (var scanner = new Scanner(D14p1.class.getResourceAsStream(file))) {
            var dataMap = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.println(s);

                var m = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)").matcher(s);
                m.find();
                var x = (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(3)) * t) % xSize;
                if (x < 0) x += xSize;
                var y = (Integer.parseInt(m.group(2)) + Integer.parseInt(m.group(4)) * t) % ySize;
                if (y < 0) y += ySize;

                var ret = Map.entry(x, y);
                System.out.format("ret=%s%n", ret);
                return ret;
            }).filter(e -> e.getKey() != xHalf && e.getValue() != yHalf).map(e -> Map.entry(e.getKey() < xHalf, e.getValue() < yHalf)).collect(Collectors.groupingBy(e -> e, Collectors.counting()));
            System.out.println(dataMap);

            System.out.println(dataMap.values().stream().reduce(1L, (l, r) -> l * r));
        }
    }
}
