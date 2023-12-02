package us.ligusan.advent.advent2023.d2;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class D2p2 {
    public static void main(final String[] args) {
        final var colorPattern = Pattern.compile("(\\d+) (red|green|blue)");

        try (var scanner = new Scanner(D2p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
//                System.out.format("s=%s ", s);

                final var ret = colorPattern.matcher(s).results().reduce(new HashMap<String, Integer>(), (map, match) -> {
                    final var value = Integer.parseInt(match.group(1));
                    map.compute(match.group(2), (k, v) -> v == null ? value : Math.max(v, value));
                    return map;
                }, (map1, map2) -> {
                    map2.forEach((k, v) -> map1.compute(k, (k1, v1) -> v1 == null ? v : Math.max(v1, v)));
                    return map1;
                }).values().stream().reduce((a, b) -> a * b).orElse(0);
                System.out.format("ret=%d\n", ret);
                return ret;
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
