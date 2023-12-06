package us.ligusan.advent.advent2023.d5;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D5p2 {
    public static void main(final String[] args) {
        final var initialSeeds = new ArrayList<Map.Entry<BigInteger, BigInteger>>();

        final var rulesList = new ArrayList<Map<Map.Entry<BigInteger, BigInteger>, BigInteger>>();

        final var rulesFlagRef = new AtomicBoolean();
        final var ruleMapRef = new AtomicReference<Map<Map.Entry<BigInteger, BigInteger>, BigInteger>>();
        try (var scanner = new Scanner(D5p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                if (rulesFlagRef.get()) {
                    if (s.isBlank()) {
                        final var ruleMap = ruleMapRef.get();
                        if (ruleMap != null) rulesList.add(ruleMapRef.get());
                    } else if (s.endsWith(":")) {
                        ruleMapRef.set(new HashMap<>());
                    } else {
                        final var ruleList = Pattern.compile("\\d+").matcher(s).results().map(matchResult -> new BigInteger(matchResult.group())).collect(Collectors.toList());
                        ruleMapRef.get().put(Map.entry(ruleList.get(1), ruleList.get(2)), ruleList.get(0));
                    }
                } else {
                    Pattern.compile("(\\d+) (\\d+)").matcher(s.split(":")[1]).results().forEach(m -> initialSeeds.add(Map.entry(new BigInteger(m.group(1)), new BigInteger(m.group(2)))));
                    rulesFlagRef.set(true);
                }
            });
        }
        rulesList.add(ruleMapRef.get());

        System.out.format("initialSeeds=%s\n", initialSeeds);
        System.out.format("rulesList=%s\n", rulesList);

        List<Map.Entry<BigInteger, BigInteger>> result = initialSeeds.stream().map(seed -> {
            final var seedStart = seed.getKey();
            return Map.entry(seedStart, seedStart.add(seed.getValue()).subtract(BigInteger.ONE));
        }).collect(Collectors.toList());

        for (Map<Map.Entry<BigInteger, BigInteger>, BigInteger> rules : rulesList) {
            System.out.format("rules=%s\n", rules);
            final var newResult = new ArrayList<Map.Entry<BigInteger, BigInteger>>();

            final var intermidiaryResult = new ArrayDeque<Map.Entry<BigInteger, BigInteger>>(result);

            while (intermidiaryResult.size() > 0) {
                final var seed = intermidiaryResult.removeFirst();

                final var seedStart = seed.getKey();
                final var seedEnd = seed.getValue();
                System.out.format("\tseed=%s\n", seed);

                boolean found = false;
                for (Map.Entry<Map.Entry<BigInteger, BigInteger>, BigInteger> rule : rules.entrySet()) {
                    System.out.format("\t\trule=%s\n", rule);

                    final var ruleKey = rule.getKey();
                    final var ruleStart = ruleKey.getKey();
                    final var ruleLength = ruleKey.getValue().subtract(BigInteger.ONE);
                    final var ruleEnd = ruleStart.add(ruleLength);
                    final var ruleValue = rule.getValue();
                    System.out.format("\t\t%s -> %s ... %s -> %s\n", ruleStart, ruleValue, ruleEnd, ruleValue.add(ruleLength));

                    final var maxStart = seedStart.max(ruleStart);
                    final var minEnd = seedEnd.min(ruleEnd);

                    found = minEnd.compareTo(maxStart) >= 0;
                    if (found) {
                        if (seedStart.compareTo(ruleStart) < 0)
                            intermidiaryResult.add(Map.entry(seedStart, ruleStart.subtract(BigInteger.ONE)));

                        final var middle = Arrays.asList(maxStart, minEnd).stream().map(i -> ruleValue.add(i.subtract(ruleStart))).collect(Collectors.toList());
                        newResult.add(Map.entry(middle.get(0), middle.get(1)));

                        if (seedEnd.compareTo(ruleEnd) > 0)
                            intermidiaryResult.add(Map.entry(ruleEnd.add(BigInteger.ONE), seedEnd));

                        break;
                    }
                }
                if (!found) newResult.add(seed);
            }

            System.out.format("newResult=%s\n", newResult);
            result = newResult;
        }

        result.stream().map(seed -> seed.getKey()).reduce(BigInteger::min).ifPresent(System.out::println);
    }
}
