package us.ligusan.advent.advent2023.d8;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D8p1 {
    public static void main(final String[] args) {
        final var instructions = new StringBuilder();
        final var nodes = new HashMap<String, List<String>>();

        final var nodesFlagRef = new AtomicBoolean();
        try (var scanner = new Scanner(D8p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s\n", s);
                if (nodesFlagRef.get()) {
                    if (!s.isBlank())
                        Pattern.compile("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)").matcher(s).results().forEach(matchResult -> nodes.put(matchResult.group(1), List.of(matchResult.group(2), matchResult.group(3))));
                } else {
                    instructions.append(s);
                    nodesFlagRef.set(true);
                }
            });
        }

        System.out.format("instructions=%s\n", instructions);
        System.out.format("nodes=%s\n", nodes);

        var index = 0;
        var node = "AAA";
        while (!"ZZZ".equals(node))
            node = nodes.get(node).get(instructions.charAt(index++ % instructions.length()) == 'R' ? 1 : 0);
        System.out.format("index=%s\n", index);
    }
}
