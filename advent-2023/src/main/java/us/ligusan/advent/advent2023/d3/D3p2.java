package us.ligusan.advent.advent2023.d3;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D3p2 {
    public static void main(final String[] args) {
        final var numbers = new ArrayList<Map.Entry<String, Map.Entry<Integer, Integer>>>();
        final var stars = new ArrayList<Map.Entry<Integer, Integer>>();

        try (var scanner = new Scanner(D3p2.class.getResourceAsStream("input.txt"))) {
            final var lineRef = new AtomicInteger();
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                final var y = lineRef.getAndIncrement();
                Pattern.compile("\\d+").matcher(s).results().forEach(m -> numbers.add(Map.entry(m.group(), Map.entry(m.start(), y))));
                Pattern.compile("\\*").matcher(s).results().forEach(m -> stars.add(Map.entry(m.start(), y)));
            });

            System.out.format("numbers=%s\nstars=%s\n", numbers, stars);
        }

        stars.stream().map(star -> {
            final var starX = star.getKey();
            final var starY = star.getValue();

            return numbers.stream().filter(number -> {
                final var numberXY = number.getValue();
                final var numberX = numberXY.getKey();
                final var numberY = numberXY.getValue();
                return starX >= numberX - 1 && starX <= numberX + number.getKey().length() && starY >= numberY - 1 && starY <= numberY + 1;
            }).map(number -> Integer.parseInt(number.getKey())).collect(Collectors.toList());
        }).filter(adjustedNumbers -> adjustedNumbers.size() == 2).map(gearNumbers -> gearNumbers.get(0) * gearNumbers.get(1)).reduce(Integer::sum).ifPresent(System.out::println);
    }
}
