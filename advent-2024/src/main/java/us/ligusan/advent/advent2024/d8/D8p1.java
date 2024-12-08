package us.ligusan.advent.advent2024.d8;

import java.util.*;
import java.util.stream.Stream;

public class D8p1 {
    public static void main(final String[] args) throws Exception {
        var antennasMap = new HashMap<Character, List<Map.Entry<Integer, Integer>>>();

        var xIndex = 0;
        var yIndex = 0;

        try (var scanner = new Scanner(D8p1.class.getResourceAsStream("input.txt"))) {
            for (String s; scanner.hasNextLine(); yIndex++) {
                s = scanner.nextLine();
                System.out.println(s);

                for (xIndex = 0; xIndex < s.length(); xIndex++) {
                    var c = s.charAt(xIndex);
                    if (c != '.') antennasMap.computeIfAbsent(c, k -> new ArrayList<>()).add(Map.entry(xIndex, yIndex));
                }
            }
        }

        final var xSize = xIndex;
        final var ySize = yIndex;
        System.out.format("xSize=%d, ySize=%d%n", xSize, ySize);
        System.out.println(antennasMap);

        var result = new HashSet<Map.Entry<Integer, Integer>>();
        antennasMap.values().forEach(list -> {
            var listSize = list.size();
            for (int i = 0; i < listSize - 1; i++) {
                var iX = list.get(i).getKey();
                var iY = list.get(i).getValue();

                for (int j = i + 1; j < listSize; j++) {
                    var jX = list.get(j).getKey();
                    var jY = list.get(j).getValue();

                    var diffX = iX - jX;
                    var diffY = iY - jY;
                    Stream.of(Map.entry(iX + diffX, iY + diffY), Map.entry(jX - diffX, jY - diffY)).filter(entry -> {
                        var x = entry.getKey();
                        var y = entry.getValue();
                        return x >= 0 && x < xSize && y >= 0 && y < ySize;
                    }).forEach(result::add);
                }
            }
        });
        System.out.println(result.size());
    }
}
