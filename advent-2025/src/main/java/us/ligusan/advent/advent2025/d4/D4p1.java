package us.ligusan.advent.advent2025.d4;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class D4p1
{
    static void main()
    {
        final List<List<Integer>> input;

        try(var scanner = new Scanner(D4p1.class.getResourceAsStream("testInput.txt")))
        {
            input = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().boxed().toList()).toList();
        }

        var xSize = input.getFirst().size();
        var ySize = input.size();

        Stream.iterate(0, y -> y < ySize, y -> y + 1).flatMapToLong(y -> {
            System.out.printf("y=%d%n", y);
            return Stream.iterate(0, x -> x < xSize, x -> x + 1).filter(x -> input.get(y).get(x) == '@').mapToLong(x -> {
                var ret = Stream.iterate(-1, i -> i < 2, i -> i + 1).mapToLong(i -> {
                    var ret1 = Stream.iterate(-1, j -> j < 2, j -> j + 1).filter(j -> i != 0 && j != 0).filter(j -> {
                        var testY = y + i;
                        var testX = x + j;
                        return testY >= 0 && testY < ySize && testX >= 0 && testX < xSize && input.get(testY).get(testX) == '@';
                    }).count();
                    System.out.printf("\t\t%d, %d: %d%n", i, j, ret1);
                    return ret1;
                }).sum();
                System.out.printf("\tx=%d: %d%n", x, ret);
                return ret;
            });
        }).sum();
    }
}
