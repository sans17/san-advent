package us.ligusan.advent.advent2023.d13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D13p1 {
    public static void main(final String[] args) {
        final var data = new ArrayList<List<Character>>();

        final var result = new AtomicInteger();
        try (var scanner = new Scanner(D13p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                if (s.isBlank()) {
                    result.addAndGet(summary(data));
                    System.out.format("result=%d\n", result.get());

                    data.clear();
                } else
                    data.add(s.chars().mapToObj(i -> (char) i).collect(Collectors.toList()));
            });
        }

        result.addAndGet(summary(data));
        System.out.format("result=%d\n", result.get());
    }

    private static int summary(final List<List<Character>> data) {
        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        Integer y = null;
        var size = data.size();
        for (int i = 0; i < size - 1; i++) {
            y = i;
            for (int j = 0; i - j >= 0 && i + j + 1 < size; j++) {
                final var row1 = data.get(i - j);
                final var row2 = data.get(i + j + 1);

//                System.out.format("i=%d, j=%d, row1(%d)=%s, row2(%d)=%s\n", i, j, i - j, row1, i + j + 1, row2);

                if (!data.get(i - j).equals(data.get(i + j + 1))) {
                    y = null;
                    break;
                }
            }
            if (y != null) break;
        }
        if (y != null) return (y +1) * 100;

        var dataTransposed = Stream.iterate(0, i -> i < data.get(0).size(), i -> i + 1).map(i -> Stream.iterate(0, j -> j < data.size(), j -> j + 1).map(j -> data.get(j).get(i)).collect(Collectors.toList())).collect(Collectors.toList());

//        System.out.format("dataTransposed=%s\n", dataTransposed);

        Integer x = null;
        size = dataTransposed.size();
        for (int i = 0; i < size - 1; i++) {
            x = i;
            for (int j = 0; i - j >= 0 && i + j + 1 < size; j++) {
//                System.out.format("i=%d, j=%d\n", i, j);
//                System.out.format("i=%d, j=%d, dataTransposed.get(i - j)(%d)=%s, dataTransposed.get(i + j + 1)(%d)=%s\n", i, j, i - j, dataTransposed.get(i - j), i + j + 1, dataTransposed.get(i + j + 1));

                if (!dataTransposed.get(i - j).equals(dataTransposed.get(i + j + 1))) {
                    x = null;
                    break;
                }
            }
            if (x != null) break;
        }

        if (x != null) return x + 1;

        System.out.format("0\n");
        return 0;
    }
}
