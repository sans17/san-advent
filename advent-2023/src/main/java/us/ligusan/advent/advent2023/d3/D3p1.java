package us.ligusan.advent.advent2023.d3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class D3p1 {
    public static void main(final String[] args) {
        final var numbers = new ArrayList<Map.Entry<String, Map.Entry<Integer, Integer>>>();
        final var symbols = new ArrayList<Map.Entry<Integer, Integer>>();

        try (var scanner = new Scanner(D3p1.class.getResourceAsStream("input.txt"))) {
            final var lineRef = new AtomicInteger();
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                final var y = lineRef.getAndIncrement();
                Pattern.compile("\\d+").matcher(s).results().forEach(m -> numbers.add(Map.entry(m.group(), Map.entry(m.start(), y))));
                Pattern.compile("[^\\d\\.]").matcher(s).results().forEach(m -> symbols.add(Map.entry(m.start(), y)));
            });

            System.out.format("numbers=%s\nsymbols=%s\n", numbers, symbols);
        }

        numbers.stream().filter(number -> {
            final var numberXY = number.getValue();
            final var numberX = numberXY.getKey();
            final int numberY = numberXY.getValue();
            return symbols.stream().anyMatch(symbol -> {
                final int symbolX = symbol.getKey();
                final int symbolY = symbol.getValue();
                return symbolX >= numberX -1 && symbolX <= numberX + number.getKey().length() && symbolY >= numberY -1 && symbolY <= numberY +1;
            });
        }).map(number -> Integer.parseInt(number.getKey())).reduce(Integer::sum).ifPresent(System.out::println);
    }
}
