package us.ligusan.advent.advent2025.d7;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D7p2 {
    static void main() {
        final List<List<Integer>> input;

        try (var scanner = new Scanner(D7p2.class.getResourceAsStream("input.txt"))) {
            input = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().boxed().toList()).toList();
        }

        var start = input.getFirst();

        var xSize = start.size();
        var ySize = input.size();

        var beamLines = new ArrayList<Set<Integer>>();

        int iStart = -1;
        for (var i = 0; i < xSize; i++)
            if ('S' == start.get(i)) {
                iStart = i;
                beamLines.add(Collections.singleton(i));
                break;
            }

        for (var i = 1; i < ySize; i++) {
            final var iFinal = i;
            var newBeams = beamLines.get(i - 1).stream().flatMap(beam -> '^' == input.get(iFinal).get(beam) ? Stream.of(beam - 1, beam + 1) : Stream.of(beam)).collect(Collectors.toSet());
            System.out.printf("%d: %s%n", i, newBeams);
            beamLines.add(newBeams);
        }

        var beamsCounters = beamLines.get(ySize - 1).stream().collect(Collectors.toMap(beam -> beam, _ -> 1L));
        System.out.println(beamsCounters);
        for (var i = ySize - 2; i >= 0; i--) {
            beamsCounters.keySet().retainAll(beamLines.get(i));
            final var iFinal = i;
            beamsCounters.putAll(beamsCounters.entrySet().stream().flatMap(e ->
                    Stream.of(-1, 1).map(delta -> {
                        final var splitBeam = e.getKey() + delta;
                        return splitBeam >= 0 && splitBeam < xSize && '^' == input.get(iFinal).get(splitBeam) ? Map.entry(splitBeam, e.getValue()) : null;
                    }).filter(Objects::nonNull)
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum)));
            System.out.printf("%d: %s%n", i, beamsCounters);
        }
        System.out.println(beamsCounters.get(iStart));
    }
}
