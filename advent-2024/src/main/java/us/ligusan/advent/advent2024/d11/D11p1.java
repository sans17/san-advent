package us.ligusan.advent.advent2024.d11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D11p1 {
    public static void main(final String[] args) throws Exception {
        Map<Long, Integer> stones;

        try(var scanner = new Scanner(D11p1.class.getResourceAsStream("input.txt"))) {
            stones = Stream.of(scanner.nextLine().split("\\s+")).map(Long::parseLong).collect(Collectors.toMap(l -> l, _ -> 1));
        }
        System.out.println(stones);

        var cache = new HashMap<>(Map.of(0L, List.of(1L)));

        for(var i = 0; i < 25; i++) {
            final var iFinal = i;
            stones = stones.entrySet().stream().flatMap(e -> cache.computeIfAbsent(e.getKey(), k -> {
                System.out.format("i=%d, k=%d%n", iFinal, k);

                var s = k.toString();
                if(s.length() % 2 == 0) {
                    var half = s.length() / 2;
                    return List.of(Long.parseLong(s.substring(0, half)), Long.parseLong(s.substring(half)));
                }
                else return List.of(k * 2024);
            }).stream().map(stone -> Map.entry(stone, e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
        }
        System.out.println(stones.values().stream().reduce(Integer::sum).get());
    }
}
