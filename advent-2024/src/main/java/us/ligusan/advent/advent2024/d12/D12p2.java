package us.ligusan.advent.advent2024.d12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class D12p2 {
    public static void main(final String[] args) throws Exception {
        var areas = new ArrayList<Map.Entry<Character, Set<Map.Entry<Integer, Integer>>>>();

        var xIndex = 0;
        var yIndex = 0;

        try(var scanner = new Scanner(D12p2.class.getResourceAsStream("input.txt"))) {
            for(String s; scanner.hasNextLine(); yIndex++) {
                s = scanner.nextLine();
                System.out.println(s);

                for(xIndex = 0; xIndex < s.length(); xIndex++) {
                    var c = s.charAt(xIndex);

                    var newArea = new HashSet<>(Set.of(Map.entry(xIndex, yIndex)));
                    for(var iterator = areas.iterator(); iterator.hasNext(); ) {
                        var entry = iterator.next();
                        if(entry.getKey() == c) {
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

        var result = 0;
        for(var area : areas) {
            var areaValue = area.getValue();
            var areaSize = areaValue.size();
//            System.out.format("area=%s, areaSize=%d%n", area, areaSize);

//            var debugMap = new HashMap<Map.Entry<Integer, Integer>, Integer>();

            var areaSides = areaValue.stream().mapToInt(e -> {
                var x = e.getKey();
                var y = e.getValue();
                var dList = List.of(-1, 1);
                var sides = dList.stream().flatMapToInt(dx -> dList.stream().mapToInt(dy -> switch((int)areaValue.stream().filter(e1 -> List.of(Map.entry(x + dx, y), Map.entry(x, y + dy)).contains(e1)).count()) {
                    case 0 -> 1;
                    case 1 -> 0;
                    case 2 -> areaValue.contains(Map.entry(x + dx, y + dy)) ? 0 : 1;
                    default -> throw new IllegalStateException("Unexpected value");
                })).sum();
//                debugMap.put(e, sides);
//                System.out.format("e=%s, sides=%d%n", e, sides);
                return sides;
            }).sum();
            result += areaSize * areaSides;

//            for(var y = areaValue.stream().mapToInt(Map.Entry::getValue).min().getAsInt(); y <= areaValue.stream().mapToInt(Map.Entry::getValue).max().getAsInt(); y++) {
//                for(var x = areaValue.stream().mapToInt(Map.Entry::getKey).min().getAsInt(); x <= areaValue.stream().mapToInt(Map.Entry::getKey).max().getAsInt(); x++) {
//                    var entry = Map.entry(x, y);
//                    System.out.print(areaValue.contains(Map.entry(x, y)) ? Integer.toString(debugMap.get(entry), 36) : '.');
//                }
//                System.out.println();
//            }
//            System.out.format("areaSides=%d, result=%d%n", areaSides, result);
        }
        System.out.println(result);
    }
}
