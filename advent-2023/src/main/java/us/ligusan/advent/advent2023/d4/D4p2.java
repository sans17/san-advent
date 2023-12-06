package us.ligusan.advent.advent2023.d4;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D4p2 {
    public static void main(final String[] args) {
        final var forwardCards = new ArrayList<Integer>();

        try (var scanner = new Scanner(D4p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
                final var numberOfCards = (forwardCards.isEmpty() ? 0 : forwardCards.remove(0)) + 1;
                System.out.format("numberOfCards=%s\n", numberOfCards);

                final var card = Arrays.stream(s.split(":")[1].split("\\|")).map(cardSide -> Pattern.compile("(\\d+)").matcher(cardSide).results().map(matchResult -> matchResult.group()).collect(Collectors.toList())).collect(Collectors.toList());
                System.out.format("card=%s\n", card);
                final var winningNumbers = card.get(0);
                winningNumbers.retainAll(card.get(1));
                System.out.format("winningNumbers=%s\n", winningNumbers);

                int i = 0;
                final var winningNumbersSize = winningNumbers.size();
                for (; i < Math.min(forwardCards.size(), winningNumbersSize); i++) {
                    forwardCards.set(i, forwardCards.get(i) + numberOfCards);
                }
                for (; i < winningNumbersSize; i++) {
                    forwardCards.add(numberOfCards);
                }

                System.out.format("forwardCards=%s\n", forwardCards);

                return numberOfCards;
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
