package us.ligusan.advent.advent2024.d12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class D12p1 {
    public static void main(final String[] args) throws Exception {
        var areas = new ArrayList<Map.Entry<Character, Set<Map.Entry<Integer, Integer>>>>();

        var xIndex = 0;
        var yIndex = 0;

        try (var scanner = new Scanner(D12p1.class.getResourceAsStream("input.txt"))) {
            for (String s; scanner.hasNextLine(); yIndex++) {
                s = scanner.nextLine();
                System.out.println(s);

                for (xIndex = 0; xIndex < s.length(); xIndex++) {
                    var c = s.charAt(xIndex);

                    var newArea = new HashSet<>(Set.of(Map.entry(xIndex, yIndex)));
                    for(var iterator = areas.iterator(); iterator.hasNext(); ) {
                        var entry = iterator.next();
                        if (entry.getKey() == c) {
                            final var xFinal = xIndex;
                            final var yFinal = yIndex;

                            var oldArea = entry.getValue();
                            if(oldArea.stream().anyMatch(e -> Math.abs(e.getKey() - xFinal) + Math.abs(e.getValue() - yFinal) == 1)) {
                                iterator.remove();
                                newArea.addAll(oldArea);
                            }
                        }
                    }
                    areas.add(Map.entry(c, newArea));
                }
            }
        }
        System.out.println(areas);

        var result = 0L;
        for(var area : areas) {
            var areaValue = area.getValue();
            result += areaValue.size() * areaValue.stream().mapToLong(e -> 4 - areaValue.stream().filter(e1 -> Math.abs(e1.getKey() - e.getKey()) + Math.abs(e1.getValue() - e.getValue()) == 1).count()).sum();
            System.out.format("area=%s, result=%d%n", area, result);
        }
        System.out.println(result);
    }
}
