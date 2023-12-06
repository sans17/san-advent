package us.ligusan.advent.advent2023.d5;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D5p2 {
    public static void main(final String[] args) {
        final var initialSeeds = new ArrayList<Range>();

        final var rulesList = new ArrayList<Map<Range, BigInteger>>();

        final var rulesFlagRef = new AtomicBoolean();
        final var ruleMapRef = new AtomicReference<Map<Range, BigInteger>>();
        try (var scanner = new Scanner(D5p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                if (rulesFlagRef.get()) {
                    if (s.isBlank()) {
                        final var ruleMap = ruleMapRef.get();
                        if (ruleMap != null) rulesList.add(ruleMap);
                        ruleMapRef.set(null);
                    } else if (s.endsWith(":")) {
                        ruleMapRef.set(new HashMap<>());
                    } else {
                        final var ruleList = Pattern.compile("\\d+").matcher(s).results().map(matchResult -> matchResult.group()).collect(Collectors.toList());
                        ruleMapRef.get().put(new Range(ruleList.get(1), ruleList.get(2)), new BigInteger(ruleList.get(0)));
                    }
                } else {
                    Pattern.compile("(\\d+) (\\d+)").matcher(s.split(":")[1]).results().forEach(m -> initialSeeds.add(new Range(m.group(1), m.group(2))));
                    rulesFlagRef.set(true);
                }
            });
        }
        final var ruleMap = ruleMapRef.get();
        if (ruleMap != null) rulesList.add(ruleMap);

        System.out.format("initialSeeds=%s\n", initialSeeds);
        System.out.format("rulesList=%s\n", rulesList);

        Map<Range, Optional<Map.Entry<Range, BigInteger>>> result = initialSeeds.stream().collect(Collectors.toMap(seed -> seed, seed -> Optional.empty()));
        for (Map<Range, BigInteger> rules : rulesList) {
            System.out.format("rules=%s\n", rules);

            for (Map.Entry<Range, BigInteger> rule : rules.entrySet()) {
                System.out.format("\trule=%s\n", rule);

                final var newResult = new HashMap<Range, Optional<Map.Entry<Range, BigInteger>>>();
                for (Map.Entry<Range, Optional<Map.Entry<Range, BigInteger>>> transformation : result.entrySet()) {
                    final var transformationKey = transformation.getKey();
                    final var transformationValue = transformation.getValue();
                    if (transformationValue.isEmpty()) {
                        transformationKey.intersect(rule.getKey()).forEach((key, value) -> newResult.put(key, value ? Optional.of(rule) : Optional.empty()));
                    } else newResult.put(transformation.getKey(), transformationValue);
                }
                result = newResult;
                System.out.format("\tresult=%s\n", result);

                if (result.values().stream().allMatch(value -> value.isPresent())) break;
            }

            result = result.entrySet().stream().collect(Collectors.toMap(transformation -> {
                final var transformationKey = transformation.getKey();
                final var transformationValue = transformation.getValue();
                if (transformationValue.isEmpty()) return transformationKey;

                final var rule = transformationValue.get();
                return transformationKey.transform(rule.getKey().getStart(), rule.getValue());
            }, transformation -> Optional.empty()));
            System.out.format("result=%s\n", result);
        }

        result.entrySet().stream().map(transformation -> transformation.getKey().getStart()).reduce(BigInteger::min).ifPresent(System.out::println);
    }
}

class Range {
    private final BigInteger start;
    private final BigInteger length;

    protected Range(final BigInteger newStart, final BigInteger newLength) {
        start = newStart;
        length = newLength;
    }
    protected Range(final String newStart, final String newLength) {
        start = new BigInteger(newStart);
        length = new BigInteger(newLength);
    }

    protected BigInteger getStart() {
        return start;
    }
    protected BigInteger getLength() {
        return length;
    }
    protected BigInteger getEnd() {
        return start.add(length).subtract(BigInteger.ONE);
    }

    public String toString() {
        return String.format("%s..%s", start, getEnd());
    }

    protected Map<Range, Boolean> intersect(final Range rule) {
        final var end = getEnd();
        final var ruleStart = rule.getStart();
        final var ruleEnd = rule.getEnd();

        final var maxStart = start.max(ruleStart);
        final var minEnd = end.min(ruleEnd);

        if (maxStart.compareTo(minEnd) > 0) return Collections.singletonMap(this, false);

        final var ret = new HashMap<Range, Boolean>();

        if (start.compareTo(ruleStart) < 0) ret.put(new Range(start, ruleStart.subtract(start)), false);

        ret.put(new Range(maxStart, minEnd.subtract(maxStart).add(BigInteger.ONE)), true);

        if (end.compareTo(ruleEnd) > 0) ret.put(new Range(ruleEnd.add(BigInteger.ONE), end.subtract(ruleEnd)), false);

        System.out.format("intersect(%s) -> %s\n", rule, ret);
        return ret;
    }

    protected Range transform(final BigInteger ruleStart, final BigInteger ruleDestination) {
        return new Range(start.add(ruleDestination.subtract(ruleStart)), length);
    }
}