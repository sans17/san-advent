package us.ligusan.advent.advent2023.d7;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D7p2 {
    public static void main(final String[] args) {
        final var data = new TreeMap<String, Integer>((leftHand, rightHand) -> {
            System.out.format(": leftHand=%s, rightHand=%s\n", leftHand, rightHand);

            final var handMaps = Stream.of(leftHand, rightHand).map(hand -> Pattern.compile(".").matcher(hand).results().map(matchResult -> matchResult.group()).collect(Collectors.groupingBy(k -> k, Collectors.counting()))).map(handMap -> {
                Long jokers = handMap.remove("J");
                if (jokers != null)
                    handMap.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).ifPresentOrElse(entry -> handMap.put(entry.getKey(), entry.getValue() + jokers), () -> handMap.put("J", jokers));
                return handMap;
            }).collect(Collectors.toList());

            final var leftMap = handMaps.get(0);
            final var rightMap = handMaps.get(1);

            System.out.format(": leftMap=%s, rightMap=%s\n", leftMap, rightMap);

            var ret = rightMap.size() - leftMap.size();
            if (ret != 0) return ret;

            final var handCounts = Stream.of(leftMap, rightMap).map(map -> map.values().stream().max(Long::compareTo).get()).collect(Collectors.toList());
            ret = Long.compare(handCounts.get(0), handCounts.get(1));
            if (ret != 0) return ret;

            for (int i = 0; i < leftHand.length(); i++) {
                final var leftChar = leftHand.charAt(i);
                final var rightChar = rightHand.charAt(i);

                if (leftChar != rightChar) return Stream.of(leftChar, rightChar).map(c -> switch (c) {
                    case 'A' -> 14;
                    case 'K' -> 13;
                    case 'Q' -> 12;
                    case 'J' -> 1;
                    case 'T' -> 10;
                    default -> c - '0';
                }).reduce((a, b) -> a - b).get();
            }

            return 0;
        });

        try (var scanner = new Scanner(D7p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s\n", s);

                final var split = s.split(" ");
                data.put(split[0], Integer.parseInt(split[1]));

                System.out.format("data=%s\n", data);
            });
        }

        final var indexRef = new AtomicInteger();
        data.values().stream().mapToInt(value -> indexRef.incrementAndGet() * value).reduce(Integer::sum).ifPresent(System.out::println);
    }
}
