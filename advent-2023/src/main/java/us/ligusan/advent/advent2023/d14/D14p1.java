package us.ligusan.advent.advent2023.d14;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D14p1 {
    public static void main(final String[] args) {
        List<List<Character>> data;

        try (var scanner = new Scanner(D14p1.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList());
        }

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        final var sizeY = data.size();
        System.out.println(Stream.iterate(0, x -> x < data.get(0).size(), x -> x + 1).mapToInt(x -> {
            final var loadCounter = new AtomicInteger(sizeY);
            final var ret = Stream.iterate(0, y -> y < sizeY, y -> y + 1).mapToInt(y -> {
                final var c = data.get(y).get(x);
                if (c == 'O')
                    return loadCounter.getAndDecrement();
                if (c == '#')
                    loadCounter.set(sizeY - y - 1);
                return 0;
            }).reduce(Integer::sum).getAsInt();
            System.out.format("x=%d, ret=%d\n", x, ret);
            return ret;
        }).reduce(Integer::sum).getAsInt());
    }
}
