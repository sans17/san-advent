package us.ligusan.advent.advent2023.d4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D4p2 {
    public static void main(final String[] args) {
        final var cards = new HashMap<Integer, Integer>();

        final var index = new AtomicInteger();
        try (var scanner = new Scanner(D4p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                var numberOfCards = cards.compute(index.getAndIncrement(), (k, v) -> (v == null ? 0 : v) + 1);

                final var card = Arrays.stream(s.split(":")[1].split("\\|")).map(cardSide -> Pattern.compile("(\\d+)").matcher(cardSide).results().map(matchResult -> matchResult.group()).collect(Collectors.toList())).collect(Collectors.toList());
                System.out.format("card=%s\n", card);
                final var winningNumbers = card.get(0);
                winningNumbers.retainAll(card.get(1));
                System.out.format("winningNumbers=%s\n", winningNumbers);
                for (int i = 0; i < winningNumbers.size(); i++)
                    cards.compute(index.get() + i, (k, v) -> (v == null ? 0 : v) + numberOfCards);

                System.out.format("cards=%s\n", cards);
            });
        }

       System.out.format("cards.values.sum=%s\n", cards.values().stream().mapToInt(Integer::intValue).sum());
    }
}
