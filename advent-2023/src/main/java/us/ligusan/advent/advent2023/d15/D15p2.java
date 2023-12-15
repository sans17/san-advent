package us.ligusan.advent.advent2023.d15;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D15p2 {
    public static void main(final String[] args) {
        final List<List<Map.Entry<String, Integer>>> boxes = Stream.iterate(0, i -> i < 256, i -> i + 1).map(i -> new ArrayList<Map.Entry<String, Integer>>()).collect(Collectors.toList());

        try (var scanner = new Scanner(D15p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter(",|\r?\n").tokens().forEach(s -> {
                if (!s.isBlank()) {
                    final var split = s.split("[=-]");
                    final var hash = split[0].chars().reduce(0, (i, c) -> (i + c) * 17 % 256);
                    System.out.format("s=%s, hash=%d\n", s, hash);

                    var box = boxes.get(hash);
                    if (s.indexOf('-') >= 0)
                        for (final Iterator<Map.Entry<String, Integer>> iterator = box.iterator(); iterator.hasNext(); ) {
                            final var entry = iterator.next();
                            if (entry.getKey().equals(split[0])) {
                                iterator.remove();
                                break;
                            }
                        }
                    else {
                        var lensValue = Integer.parseInt(split[1]);
                        var found = false;
                        for (int i = 0; i < box.size(); i++)
                            if (found = box.get(i).getKey().equals(split[0])) {
                                box.set(i, Map.entry(split[0], lensValue));
                                break;
                            }
                        if (!found) box.add(Map.entry(split[0], lensValue));
                    }
                }
            });
        }
        System.out.format("boxes=%s\n", boxes);

        var counter = 0;
        for (int i = 0; i < 256; i++) {
            final var box = boxes.get(i);
            for (int j = 0; j < box.size(); j++) {
                counter += (i + 1) * (j + 1) * box.get(j).getValue();
            }
        }
        System.out.format("counter=%d\n", counter);
    }
}
