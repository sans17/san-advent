package us.ligusan.advent.advent2023.d19;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D19p2 {
    public static void main(final String[] args) {
        final var instructions = new HashMap<String, List<Instruction>>();

        final var partsFlagRef = new AtomicBoolean();
        try (var scanner = new Scanner(D19p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s\n", s);
                if (!partsFlagRef.get())
                    if (s.isBlank())
                        partsFlagRef.set(true);
                    else {
                        final var matchResult = Pattern.compile("([a-z]+)\\{(.+)\\}").matcher(s).results().findFirst().get();
                        instructions.put(matchResult.group(1), Arrays.stream(matchResult.group(2).split(",")).map(ruleString -> {
                            if (ruleString.indexOf(':') < 0)
                                return new Instruction(null, null, null, ruleString);
                            final var ruleMatchResult = Pattern.compile("([xmas])([<>])(\\d+):([a-zA-Z]+)").matcher(ruleString).results().findFirst().get();
                            return new Instruction(ruleMatchResult.group(1), ruleMatchResult.group(2), Integer.parseInt(ruleMatchResult.group(3)), ruleMatchResult.group(4));
                        }).collect(Collectors.toList()));
                    }
            });
        }
        System.out.format("instructions=%s\n", instructions);

        final var accepted = new ArrayList<Map<String, Map.Entry<Integer, Integer>>>();
        var processingList = Collections.singletonList(Map.entry("in", Map.of("x", Map.entry(0, 4001), "m", Map.entry(0, 4001), "a", Map.entry(0, 4001), "s", Map.entry(0, 4001))));
        for (; ; ) {
            System.out.format("processingList=%s\n", processingList);

            if (processingList.isEmpty())
                break;

            final var newProcessingList = new ArrayList<Map.Entry<String, Map<String, Map.Entry<Integer, Integer>>>>();
            for (final var entry : processingList) {
                final var listName = entry.getKey();
                var partsMap = entry.getValue();
                System.out.format("listName=%s, partsMap=%s\n", listName, partsMap);

                if ("A".equals(listName)) {
                    accepted.add(partsMap);
                    continue;
                }
                if ("R".equals(listName))
                    continue;

                final var instructionList = instructions.get(listName);
                for (final var instruction : instructionList) {
                    System.out.format("\tinstruction=%s, partsMap=%s\n", instruction, partsMap);

                    final var category = instruction.category();
                    final var target = instruction.target();
                    if (category == null) {
                        newProcessingList.add(Map.entry(target, partsMap));
                        break;
                    }

                    final var partEntry = partsMap.get(category);
                    final var partMin = partEntry.getKey();
                    final var partMax = partEntry.getValue();

                    final var ruleValue = instruction.value();
                    final var lessThan = "<".equals(instruction.rule());

                    final var trueEntry = lessThan ? Map.entry(partMin, Math.min(partMax, ruleValue)) : Map.entry(Math.max(partMin, ruleValue), partMax);
                    if (trueEntry.getKey() < trueEntry.getValue() - 1) {
                        final var newPartsMap = new HashMap<>(partsMap);
                        newPartsMap.put(category, trueEntry);
                        newProcessingList.add(Map.entry(target, newPartsMap));
                    }

                    final var falseEntry = lessThan ? Map.entry(Math.max(partMin, ruleValue - 1), partMax) : Map.entry(partMin, Math.min(partMax, ruleValue + 1));
                    if (falseEntry.getKey() < falseEntry.getValue() - 1) {
                        final var newPartsMap = new HashMap<>(partsMap);
                        newPartsMap.put(category, falseEntry);
                        partsMap = newPartsMap;
                    }

                    if (partsMap.isEmpty())
                        break;
                }
            }

            processingList = newProcessingList;
        }
        System.out.format("accepted=%s\n", accepted);

        final var intersectionsList = new ArrayList<Set<Map<String, Map.Entry<Integer, Integer>>>>();
        intersectionsList.add(new HashSet<>(accepted));
        for(int k = 0;; k++) {
            final var intersectionsSet = intersectionsList.get(intersectionsList.size() - 1);
            System.out.format("k=%d, intersectionsSet=%s\n", k, intersectionsSet);
            if(intersectionsSet.size() <= 1)
                break;

            final var nextIntersectionsSet = new HashSet<Map<String, Map.Entry<Integer, Integer>>>();

            final var intersections = new ArrayList<>(intersectionsSet);
            final var size = intersections.size();
            for (int i = 0; i < size - 2; i++)
                for (int j = i + 1; j < size - 1; j++) {
                    final var intersection = new HashMap<String, Map.Entry<Integer, Integer>>();
                    for (final var category : List.of("x", "m", "a", "s")) {
                        final var entry1 = intersections.get(i).get(category);
                        final var entry2 = intersections.get(j).get(category);
                        final var intersectionEntry = Map.entry(Math.max(entry1.getKey(), entry2.getKey()), Math.min(entry1.getValue(), entry2.getValue()));
                        if (intersectionEntry.getKey() >= intersectionEntry.getValue() - 1)
                            break;

                        intersection.put(category, intersectionEntry);
                    }

                    if(intersection.size() == 4)
                        nextIntersectionsSet.add(intersection);
                }

            intersectionsList.add(nextIntersectionsSet);
        }
        System.out.format("intersectionsList=%s\n", intersectionsList);

        var result = BigInteger.ZERO;
        for (int i = 0; i < intersectionsList.size(); i++) {
            final var intersectionsSet = intersectionsList.get(i);
            if(!intersectionsSet.isEmpty()) {
                final var area = intersectionsList.get(i).stream().map(entry -> entry.values().stream().map(categoryEntry -> categoryEntry.getValue() - categoryEntry.getKey() - 1).reduce(BigInteger.ONE, (a, b) -> a.multiply(BigInteger.valueOf(b)), (a, b) -> a.multiply(b))).reduce((a, b) -> a.add(b)).get();
                result = result.add(area.multiply(BigInteger.valueOf(i % 2 == 0 ? 1 : -1)));
            }
        }
        System.out.format("result=%s\n", result);
    }
}


