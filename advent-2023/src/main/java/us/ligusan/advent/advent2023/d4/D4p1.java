package us.ligusan.advent.advent2023.d4;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D4p1 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D4p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
                final var card = Arrays.stream(s.split(":")[1].split("\\|")).map(cardSide -> Pattern.compile("(\\d+)").matcher(cardSide).results().map(matchResult -> matchResult.group()).collect(Collectors.toList())).collect(Collectors.toList());
                System.out.format("card=%s\n", card);
                final var winningNumbers = card.get(0);
                System.out.format("winningNumbers=%s\n", winningNumbers);
                winningNumbers.retainAll(card.get(1));
                return (int) Math.pow(2, winningNumbers.size() - 1);
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
