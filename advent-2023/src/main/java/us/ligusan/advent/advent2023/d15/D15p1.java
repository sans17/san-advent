package us.ligusan.advent.advent2023.d15;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D15p1 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D15p1.class.getResourceAsStream("input.txt"))) {
            System.out.println(scanner.useDelimiter(",|\r?\n").tokens().mapToInt(s -> {
                if (s.isBlank()) return 0;
                final var hash = s.chars().reduce(0, (i, c) -> (i + c) * 17 % 256);
                System.out.format("s=%s, hash=%d\n", s, hash);
                return hash;
            }).reduce(Integer::sum).getAsInt());
        }
    }
}
