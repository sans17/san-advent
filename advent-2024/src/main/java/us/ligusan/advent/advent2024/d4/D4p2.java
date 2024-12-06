package us.ligusan.advent.advent2024.d4;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D4p2 {
    public static void main(final String[] args) throws Exception {
        final List<List<Character>> data;

        try(var scanner = new Scanner(D4p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens()
             .map(s -> s.chars().mapToObj(i -> (char)i).collect(Collectors.toList())).collect(Collectors.toList());
        }
        System.out.println(data);

        var xSize = data.get(0).size();
        var ySize = data.size();

        var testList = List.of(List.of('M', 'S'), List.of('S', 'M'));

        var counter = new AtomicInteger();
        Stream.iterate(1, y -> y < ySize - 1, y -> y + 1).forEach(y -> {
            Stream.iterate(1, x -> x < xSize - 1, x -> x + 1).forEach(x -> {
                if(data.get(y).get(x) == 'A' && testList.containsAll(
                 List.of(List.of(data.get(y - 1).get(x - 1), data.get(y + 1).get(x + 1)),
                  List.of(data.get(y + 1).get(x - 1), data.get(y - 1).get(x + 1))))) {
                    System.out.format("x=%d, y=%d%n", x, y);
                    counter.incrementAndGet();
                }
            });
        });
        System.out.println(counter);
    }
}
