package us.ligusan.advent.advent2025.d7;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D7p1 {
    static void main() {
        final List<List<Integer>> input;

        try (var scanner = new Scanner(D7p1.class.getResourceAsStream("input.txt"))) {
            input = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().boxed().toList()).toList();
        }

        var start = input.getFirst();

        var xSize = start.size();
        var ySize = input.size();

        Set<Integer> beams = new HashSet<>();

        for (var i = 0; i < xSize; i++)
            if ('S' == start.get(i)) {
                beams.add(i);
                break;
            }
        System.out.println(beams);

        var counter = new AtomicInteger();
        for (int i = 1; i < ySize; i++) {
            final var iFinal = i;
            beams = beams.stream().flatMap(beam -> {
                if ('^' == input.get(iFinal).get(beam)) {
                    counter.incrementAndGet();
                    return Stream.of(beam - 1, beam + 1);
                } else return Stream.of(beam);
            }).collect(Collectors.toSet());
        }
        System.out.println(counter);
    }
}
