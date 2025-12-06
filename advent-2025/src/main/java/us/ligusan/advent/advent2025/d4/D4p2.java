package us.ligusan.advent.advent2025.d4;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class D4p2 {
    static void main() {
        final Set<List<Integer>> input;

        AtomicInteger yRef = new AtomicInteger();
        AtomicInteger xRef = new AtomicInteger();
        try (var scanner = new Scanner(D4p2.class.getResourceAsStream("input.txt"))) {
            input = scanner.useDelimiter("\r?\n").tokens().flatMap(s -> {
                var y = yRef.getAndIncrement();
                xRef.set(0);
                return s.chars().boxed().map(c -> {
                    var x = xRef.getAndIncrement();
                    return c == '@' ? List.of(x, y) : null;
                }).filter(Objects::nonNull);
            }).collect(Collectors.toSet());
        }

        for (Set<List<Integer>> oldState = input; ; ) {
//            System.out.println(oldState);

            final var oldStateFinal = oldState;
            final var newState = oldState.stream().filter(xy -> {
                        var count = oldStateFinal.stream().filter(xy1 -> {
                            int dx = Math.abs(xy.get(0) - xy1.get(0));
                            int dy = Math.abs(xy.get(1) - xy1.get(1));
                            return (dx <= 1 && dy <= 1) && (dx != 0 || dy != 0);
                        }).count();
//                        System.out.printf("%s: %d%n", xy, count);
                        return count >= 4;
                    }
            ).collect(Collectors.toSet());

            if (oldState.size() == newState.size()) {
                System.out.println(input.size() - newState.size());
                return;
            }
            oldState = newState;
        }
    }
}
