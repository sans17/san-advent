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

                final var list = Pattern.compile("-?\\d+").matcher(s).results().map(matchResult -> Integer.parseInt(matchResult.group())).collect(Collectors.toList());
                System.out.format("list=%s\n", list);

                var listRef = new AtomicReference<List<Integer>>();

                var runningList = Stream.generate(() -> {
                    final var oldList = listRef.get();

                    List<Integer> newList;
                    if (oldList == null) newList = list;
                    else {
                        newList = new ArrayList<>();
                        for (var i = 0; i < oldList.size() - 1; i++)
                            newList.add(oldList.get(i + 1) - oldList.get(i));
                    }
                    listRef.set(newList);

                    System.out.format("\tnewList=%s\n", newList);
                    return newList;
                }).takeWhile(newList -> newList.stream().anyMatch(i -> i != 0)).map(newList -> newList.get(0)).collect(Collectors.toList());

                for (var i = runningList.size() - 1; i >= 1; i--)
                    runningList.set(i - 1, runningList.get(i - 1) - runningList.get(i));
                System.out.format("runningList=%s\n", runningList);

                return runningList.get(0);
            }).reduce(BigInteger.ZERO, (a, b) -> a.add(BigInteger.valueOf(b)), (a, b) -> a.add(b)));
        }
    }
}
