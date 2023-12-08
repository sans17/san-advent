package us.ligusan.advent.advent2023.d7;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D7p1 {
    public static void main(final String[] args) {
        final var data = new TreeMap<String, Integer>((leftHand, rightHand) -> {
            System.out.format(": leftHand=%s, rightHand=%s\n", leftHand, rightHand);

            final var handMaps = Arrays.asList(leftHand, rightHand).stream().map(hand -> Pattern.compile(".").matcher(hand).results().map(matchResult -> matchResult.group()).reduce(new HashMap<String, Integer>(), (stringIntegerMap, s) -> {
                stringIntegerMap.compute(s, (k, v) -> v == null ? 1 : v + 1);
                return stringIntegerMap;
            }, (map1, map2) -> {
                map2.forEach((k, v) -> map1.compute(k, (k1, v1) -> v1 == null ? v : v + v1));
                return map1;
            })).collect(Collectors.toList());

            final var leftMap = handMaps.get(0);
            final var rightMap = handMaps.get(1);

            System.out.format(": leftMap=%s, rightMap=%s\n", leftMap, rightMap);

            var ret = rightMap.size() - leftMap.size();
            if (ret != 0) return ret;

            ret = Arrays.asList(leftMap, rightMap).stream().mapToInt(map -> map.values().stream().max(Integer::compareTo).get()).reduce((a, b) -> a - b).getAsInt();
            if (ret != 0) return ret;

            for (int i = 0; i < leftHand.length(); i++) {
                final var leftChar = leftHand.charAt(i);
                final var rightChar = rightHand.charAt(i);

                if (leftChar != rightChar) return Arrays.asList(leftChar, rightChar).stream().map(c -> switch (c) {
                    case 'A' -> 14;
                    case 'K' -> 13;
                    case 'Q' -> 12;
                    case 'J' -> 11;
                    case 'T' -> 10;
                    default -> c - '0';
                }).reduce((a, b) -> a - b).get();
            }

            return 0;
        });

        try (var scanner = new Scanner(D7p1.class.getResourceAsStream("input.txt"))) {
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
