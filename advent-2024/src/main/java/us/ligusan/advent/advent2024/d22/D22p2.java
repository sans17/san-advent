package us.ligusan.advent.advent2024.d22;

import java.util.*;
import java.util.stream.Collectors;

public class D22p2 {
    private static final int LIMIT = 16_777_216;

    public static void main(final String[] args) throws Exception {
//        var file = "testInput1.txt";
//        var interations = 10;
//        var file = "testInput.txt";
        var file = "input.txt";
//        var file = "testInput2.txt";
        var interations = 2_000;
        var diffsLength = 4;

        var listOfPrices = new ArrayList<Map<List<Integer>, Integer>>();

        try (var scanner = new Scanner(D22p2.class.getResourceAsStream(file))) {
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                System.out.println(s);

                var diffs = new ArrayList<Integer>();
                var diffsMap = new HashMap<List<Integer>, Integer>();
                listOfPrices.add(diffsMap);

                var d = Long.parseLong(s);
                var nextPrice = (int) (d % 10);

                for (int i = 0; i < interations; i++) {
                    var prevPrice = nextPrice;

                    d = ((d * 64) ^ d) % LIMIT;
                    d = ((d / 32) ^ d) % LIMIT;
                    d = ((d * 2048) ^ d) % LIMIT;

                    nextPrice = (int) (d % 10);
                    diffs.add(nextPrice - prevPrice);
                    if (diffs.size() > diffsLength) diffs.removeFirst();
                    if (diffs.size() == diffsLength) diffsMap.putIfAbsent(new ArrayList<>(diffs), nextPrice);
                }
//                System.out.println(data);
            }

            System.out.println(listOfPrices.stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toSet()).stream().mapToInt(l -> listOfPrices.stream().mapToInt(m -> m.getOrDefault(l, 0)).sum()).max().getAsInt());
        }
    }
}
