package us.ligusan.advent.advent2023.d8;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D8p2 {
    public static void main(final String[] args) {
        final var instructions = new StringBuilder();
        final var nodesMap = new HashMap<String, List<String>>();

        final var nodesFlagRef = new AtomicBoolean();
        try (var scanner = new Scanner(D8p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
//                System.out.format("s=%s\n", s);
                if (nodesFlagRef.get()) {
                    if (!s.isBlank()) {
                        Pattern.compile("([A-Z0-9]+) = \\(([A-Z0-9]+), ([A-Z0-9]+)\\)").matcher(s).results().forEach(matchResult -> nodesMap.put(matchResult.group(1), List.of(matchResult.group(2), matchResult.group(3))));

//                        System.out.format("nodesMap=%s\n", nodesMap);
                    }
                } else {
                    instructions.append(s);
                    nodesFlagRef.set(true);
                }
            });
        }

        System.out.format("instructions=%s\n", instructions);
        System.out.format("nodesMap=%s\n", nodesMap);

        final var nodes = nodesMap.keySet().stream().filter(node -> node.endsWith("A")).collect(Collectors.toList());

        final var zNodesMap = nodes.stream().collect(Collectors.toMap(node -> node, node -> new HashMap<String, List<Integer>>()));

        for (final var nodeToTest : nodes) {
            System.out.format("nodeToTest=%s\n", nodeToTest);

            var index = 0;
            var node = nodeToTest;
            for(;; index++) {
                if (node.charAt(node.length() - 1) == 'Z') {
                    System.out.format("\tnode=%s\n", node);

                    final var startStopMap = zNodesMap.get(nodeToTest);
                    var startStop = startStopMap.get(node);
                    if (startStop == null) {
                        startStop = new ArrayList<>();
                        startStop.add(index);
                        startStopMap.put(node, startStop);
                    } else {
                        startStop.add(index);
                        break;
                    }
                }

                final var instruction = instructions.charAt(index % instructions.length());

                node = nodesMap.get(node).get(instruction == 'R' ? 1 : 0);
            }
        }

        System.out.format("zNodesMap=%s\n", zNodesMap);

        // creates list of primes and factor the numbers
        final var primes = new ArrayList<Integer>();
        primes.add(2);
        zNodesMap.values().stream().map(map -> map.values().stream().findFirst().get().get(0)).map(n -> {
            final var nRemainderRef = new AtomicInteger(n);

            return Stream.iterate(0, i -> i + 1).map(index -> {
                final var primeSize = primes.size();
                if (index < primeSize) return primes.get(index);
                else for (var nextPrime = primes.get(primeSize - 1) + 1; ; nextPrime++) {
                    final var nextPrimeFinal = nextPrime;
                    if (primes.stream().takeWhile(prime -> prime * prime <= nextPrimeFinal).anyMatch(prime -> nextPrimeFinal % prime == 0))
                        continue;

                    primes.add(nextPrime);
                    return nextPrime;
                }
            }).takeWhile(prime -> nRemainderRef.get() > 1).<Integer>mapMulti((prime, consumer) -> {
                var nReminderInteger = nRemainderRef.get();
                final var divisor = prime * prime <= n ? prime : nReminderInteger;
                while (nReminderInteger % divisor == 0) {
                    consumer.accept(divisor);
                    nReminderInteger /= divisor;
                }
                nRemainderRef.set(nReminderInteger);
            }).collect(Collectors.groupingBy(prime -> prime, Collectors.counting()));
        }).flatMap(map -> map.entrySet().stream()).collect(Collectors.groupingBy(entry -> entry.getKey(), Collectors.reducing(0L, entry -> entry.getValue(), Long::max))).entrySet().stream().map(entry -> {
            var ret = BigInteger.ONE;
            for (var i = 0; i < entry.getValue(); i++) ret = ret.multiply(BigInteger.valueOf(entry.getKey()));
            return ret;
        }).reduce((a, b) -> a.multiply(b)).ifPresent(System.out::println);
    }
}
