package us.ligusan.advent.advent2023.d5;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D5p1 {
    public static void main(final String[] args) {
        final var initialSeeds = new ArrayList<BigInteger>();

        final var rulesList = new ArrayList<Map<Map.Entry<BigInteger, BigInteger>, BigInteger>>();

        final var numberPattern = Pattern.compile("\\d+");

        final var rulesFlagRef = new AtomicBoolean();
        final var ruleMapRef = new AtomicReference<Map<Map.Entry<BigInteger, BigInteger>, BigInteger>>();
        try (var scanner = new Scanner(D5p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                if (rulesFlagRef.get()) {
                    if (s.isBlank()) {
                        final var ruleMap = ruleMapRef.get();
                        if (ruleMap != null) rulesList.add(ruleMapRef.get());
                    } else if (s.endsWith(":")) {
                        ruleMapRef.set(new HashMap<>());
                    } else {
                        final var ruleList = numberPattern.matcher(s).results().map(matchResult -> new BigInteger(matchResult.group())).collect(Collectors.toList());
                        ruleMapRef.get().put(Map.entry(ruleList.get(1), ruleList.get(2)), ruleList.get(0));
                    }
                } else {
                    numberPattern.matcher(s.split(":")[1]).results().forEach(m -> initialSeeds.add(new BigInteger(m.group())));
                    rulesFlagRef.set(true);
                }
            });
        }
        rulesList.add(ruleMapRef.get());

        System.out.format("initialSeeds=%s\n", initialSeeds);
        System.out.format("rulesList=%s\n", rulesList);

        initialSeeds.stream().map(seed -> {
            System.out.format("seed=%d\n", seed);

            final var resultRef = new AtomicReference<BigInteger>(seed);
            rulesList.stream().forEach(rules -> {
                final var result = resultRef.get();
                System.out.format("\tresult=%d\n", result);

                resultRef.set(rules.entrySet().stream().filter(rule -> {
                    final var ruleKey = rule.getKey();
                    final var ruleStart = ruleKey.getKey();
                    return result.compareTo(ruleStart) >= 0  && result.compareTo(ruleStart.add(ruleKey.getValue())) < 0;
                }).findFirst().map(rule -> result.subtract(rule.getKey().getKey()).add(rule.getValue())).orElse(result));
            });
            return resultRef.get();
        }).reduce(BigInteger::min).ifPresent(System.out::println);
    }
}
