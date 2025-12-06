package us.ligusan.advent.advent2025.d4;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class D4p1 {
    static void main() {
        final List<List<Integer>> input;

        try (var scanner = new Scanner(D4p1.class.getResourceAsStream("input.txt"))) {
            input = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().boxed().toList()).toList();
        }

        var xSize = input.getFirst().size();
        var ySize = input.size();

        System.out.println(Stream.iterate(0, y -> y < ySize, y -> y + 1).flatMapToLong(y -> Stream.iterate(0, x -> x < xSize, x -> x + 1).filter(x -> input.get(y).get(x) == '@').mapToLong(x -> Stream.iterate(-1, i -> i < 2, i -> i + 1).mapToInt(i -> y + i).filter(testY -> testY >= 0 && testY < ySize).mapToLong(testY -> Stream.iterate(-1, j -> j < 2, j -> j + 1).mapToInt(j -> x + j).filter(testX -> testX >= 0 && testX < xSize).filter(testX -> testY != y || testX != x).filter(testX -> input.get(testY).get(testX) == '@').count()).sum()).filter(count -> count < 4)).count());
    }
}
