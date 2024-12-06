package us.ligusan.advent.advent2024.d4;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class D4p1 {
    public static void main(final String[] args) throws Exception {
        final List<List<Character>> data;

        try (var scanner = new Scanner(D4p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> (char) i).toList()).toList();
        }
        System.out.println(data);

        var xSize = data.getFirst().size();
        var ySize = data.size();

        var length = "XMAS".length();

        var counter = new AtomicInteger();
        Stream.iterate(0, y -> y < ySize, y -> y + 1).forEach(y -> Stream.iterate(0, x -> x < xSize, x -> x + 1).forEach(x -> {
                    if (data.get(y).get(x) == 'X') for (int i = -1; i < 2; i++)
                        for (int j = -1; j < 2; j++)
                            if (i != 0 || j != 0) {
                                var iFinal = i;
                                var jFinal = j;
                                var xEnd = x + i * (length - 1);
                                var yEnd = y + j * (length - 1);

                                if (xEnd >= 0 && xEnd < xSize && yEnd >= 0 && yEnd < ySize && "MAS".equals(
                                        Stream.iterate(1, k -> k < length, k -> k + 1)
                                                .map(k -> data.get(y + jFinal * k).get(x + iFinal * k))
                                                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString())) {
                                    System.out.format("x=%d, y=%d, i=%d, j=%d%n", x, y, i, j);
                                    counter.incrementAndGet();
                                }
                            }
                })
        );
        System.out.println(counter);
    }
}
