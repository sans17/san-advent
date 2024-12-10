package us.ligusan.advent.advent2024.d10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class D10p2 {
    public static void main(final String[] args) throws Exception {
        var data = new HashMap<Integer, List<Map.Entry<Integer, Integer>>>();

        final var xIndex = new AtomicInteger();
        final var yIndex = new AtomicInteger();

        try (var scanner = new Scanner(D10p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                xIndex.set(0);

                s.chars().forEach(c -> data.computeIfAbsent(c - '0', _ -> new ArrayList<>()).add(Map.entry(xIndex.getAndIncrement(), yIndex.get())));
                yIndex.incrementAndGet();
            });
        }
//        System.out.println(data);

        var resultMap = data.get(9).stream().collect(Collectors.toUnmodifiableMap(e -> e, _ -> 1));
        for(var i = 8; i >= 0; i--) {
//            System.out.println(resultMap);
            final var tempMap = resultMap;
            resultMap = data.get(i).stream().map(e -> Map.entry(e, tempMap.entrySet().stream().filter(e1 -> {
                var e1Key = e1.getKey();
                return Math.abs(e1Key.getKey() - e.getKey()) + Math.abs(e1Key.getValue() - e.getValue()) == 1;
            }).mapToInt(Map.Entry::getValue).sum())).collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        }
//        System.out.println(resultMap);
        System.out.println(resultMap.values().stream().mapToInt(i -> i).sum());
    }
}
