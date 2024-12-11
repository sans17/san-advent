package us.ligusan.advent.advent2024.d11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D11p2 {
    public static void main(final String[] args) throws Exception {
        Map<Long, Long> stones;

        try(var scanner = new Scanner(D11p2.class.getResourceAsStream("input.txt"))) {
            stones = Stream.of(scanner.nextLine().split("\\s+")).map(Long::parseLong).collect(Collectors.toUnmodifiableMap(l -> l, _ -> 1L));
        }
        System.out.println(stones);

        var cache = new HashMap<>(Map.of(0L, List.of(1L)));

        for(var i = 0; i < 75; i++) {
            stones = stones.entrySet().stream().flatMap(e -> cache.computeIfAbsent(e.getKey(), k -> {
                var s = k.toString();
                if(s.length() % 2 == 0) {
                    var half = s.length() / 2;
                    return List.of(Long.parseLong(s.substring(0, half)), Long.parseLong(s.substring(half)));
                }
                else return List.of(k * 2024);
            }).stream().map(l -> Map.entry(l, e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
            System.out.format("i=%d, stones=%s%n", i, stones);
        }
        System.out.println(stones.values().stream().reduce(Long::sum).get());
    }
}
