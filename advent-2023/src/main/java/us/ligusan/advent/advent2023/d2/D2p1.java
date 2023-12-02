package us.ligusan.advent.advent2023.d2;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class D2p1 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D2p1.class.getResourceAsStream("input.txt"))) {
            var linesCounter = new AtomicInteger();
            scanner.useDelimiter("\r?\n").tokens().map(s -> Map.entry(linesCounter.getAndIncrement(), s));
        }
    }
}
