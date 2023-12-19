package us.ligusan.advent.advent2023.d19;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D19p1 {
    public static void main(final String[] args) {
        final var instructions = new HashMap<String, List<Instruction>>();
        final var parts = new ArrayList<Part>();

        final var partsFlagRef = new AtomicBoolean();
        try (var scanner = new Scanner(D19p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s\n", s);
                if (partsFlagRef.get()) {
                    final var partMatchResult = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}").matcher(s).results().findFirst().get();
                    parts.add(new Part(Integer.parseInt(partMatchResult.group(1)), Integer.parseInt(partMatchResult.group(2)), Integer.parseInt(partMatchResult.group(3)), Integer.parseInt(partMatchResult.group(4))));
                } else if (s.isBlank())
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
        System.out.format("parts=%s\n", parts);

        final var accepted = new ArrayList<Part>();
        for (final var part : parts) {
            var listName = "in";
            while (!Arrays.asList("A", "R").contains(listName)) {
                final var instructionList = instructions.get(listName);
                for (final var instruction : instructionList) {
                    final var category = instruction.category();
                    final var target = instruction.target();
                    if (category == null) {
                        listName = target;
                        break;
                    }

                    final var partValue = switch (category) {
                        case "x" -> part.x();
                        case "m" -> part.m();
                        case "a" -> part.a();
                        default -> part.s();
                    };
                    final var ruleValue = instruction.value();
                    final var lessThan = "<".equals(instruction.rule());
                    final var leftValue = lessThan ? partValue : ruleValue;
                    final var rightValue = lessThan ? ruleValue : partValue;
                    if (leftValue < rightValue) {
                        listName = target;
                        break;
                    }
                }
            }
            if ("A".equals(listName))
                accepted.add(part);
        }
        System.out.format("accepted=%s\n", accepted);

        System.out.println(accepted.stream().mapToInt(part -> part.x() + part.m() + part.a() + part.s()).sum());
    }
}

record Part(int x, int m, int a, int s) {
}
