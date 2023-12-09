package us.ligusan.advent.advent2023.d9;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D9p2 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D9p2.class.getResourceAsStream("input.txt"))) {
            System.out.println(scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.format("s=%s\n", s);

                final var runningList = Stream.iterate(Pattern.compile("-?\\d+").matcher(s).results().map(matchResult -> Integer.parseInt(matchResult.group())).collect(Collectors.toList()), list -> list.stream().anyMatch(i -> i != 0), list -> {
                    final var newList = new ArrayList<Integer>();
                    for (var i = 0; i < list.size() - 1; i++)
                        newList.add(list.get(i + 1) - list.get(i));
                    return newList;
                }).map(list -> list.get(0)).collect(Collectors.toList());

                for (var i = runningList.size() - 1; i >= 1; i--)
                    runningList.set(i - 1, runningList.get(i - 1) - runningList.get(i));
                System.out.format("runningList=%s\n", runningList);

                return runningList.get(0);
            }).reduce(BigInteger.ZERO, (a, b) -> a.add(BigInteger.valueOf(b)), (a, b) -> a.add(b)));
        }
    }
}
