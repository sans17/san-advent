package us.ligusan.advent.advent2025.d9;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class D9p1
{
    static void main()
    {
        var filename = "input.txt";

        final List<List<Integer>> input;
        try(var scanner = new Scanner(D9p1.class.getResourceAsStream(filename)))
        {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).map(s -> Arrays.stream(s.trim().split(",")).map(Integer::parseInt).toList()).toList();
        }
        final var size = input.size();

        System.out.println(Stream.iterate(0, i -> i < size - 1, i -> i + 1).flatMapToLong(i -> {
            var p0 = input.get(i);
            return Stream.iterate(i + 1, j -> j < size, j -> j + 1).mapToLong(j -> Stream.iterate(0, k -> k < p0.size(), k -> k + 1).mapToLong(k -> Math.abs(p0.get(k) - input.get(j).get(k)) + 1).reduce(1, (a, b) -> a * b));
        }).max().orElse(0));
    }
}
