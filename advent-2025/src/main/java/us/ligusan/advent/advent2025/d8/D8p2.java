package us.ligusan.advent.advent2025.d8;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D8p2
{
    static void main()
    {
//        var filename = "testInput.txt";
        var filename = "input.txt";

        final List<List<Integer>> input;
        try(var scanner = new Scanner(D8p2.class.getResourceAsStream(filename)))
        {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).map(s -> Arrays.stream(s.trim().split(",")).map(Integer::parseInt).toList()).toList();
        }
        final var size = input.size();

        var connections = Stream.iterate(0, i -> i < size, i -> i + 1).map(Collections::singleton).collect(Collectors.toSet());

        for(var pair : Stream.iterate(0, i -> i < size - 1, i -> i + 1).flatMap(i -> Stream.iterate(i + 1, j -> j < size, j -> j + 1).map(j -> List.of(i, j))).sorted(Comparator.comparing(l -> {
            var p0 = input.get(l.getFirst());
            var ret = Stream.iterate(0, k -> k < p0.size(), k -> k + 1).map(k -> p0.get(k) - input.get(l.getLast()).get(k)).map(x -> (long)x * x).reduce(Long::sum).orElse(0L);
//            System.out.printf("%d, %d: %s%n", a1, a2, ret);
            return ret;
        })).toList()) {
            var cs = connections.stream().filter(c -> c.contains(pair.getFirst()) || c.contains(pair.getLast())).collect(Collectors.toSet());
            connections.removeAll(cs);
            connections.add(cs.stream().flatMap(Collection::stream).collect(Collectors.toSet()));

            if (connections.size() == 1) {
                System.out.println(pair);
                System.out.println(pair.stream().mapToLong(i -> input.get(i).getFirst()).reduce(1L, (a, b) -> a * b));
                return;
            }
        }
    }
}
