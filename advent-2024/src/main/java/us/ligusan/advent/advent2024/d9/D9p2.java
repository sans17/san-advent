package us.ligusan.advent.advent2024.d9;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class D9p2 {
    public static void main(final String[] args) throws Exception {
        List<Map.Entry<Integer, Integer>> data;

        var counterRef = new AtomicInteger();
        try(var scanner = new Scanner(D9p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.nextLine().chars().mapToObj(c -> {
                var counter = counterRef.getAndIncrement();
                return Map.entry(c - '0', counter % 2 == 0 ? counter / 2 : -1);
            }).filter(e -> e.getKey() != 0 || e.getValue() >= 0).collect(Collectors.toList());
        }
        System.out.println(data);

        for(int k = data.size() - 1; k >= 0; k--) {
            var kEntry = data.get(k);
            var kKey = kEntry.getKey();
            if(kEntry.getValue() >= 0) {
                for(int i = 0; i < k; i++) {
                    var iEntry = data.get(i);
                    if(iEntry.getValue() < 0) {
                        var iKey = iEntry.getKey();
                        if(kKey <= iKey) {
                            data.set(k, Map.entry(kKey, -1));
                            data.set(i, kEntry);
                            if(kKey < iKey) {
                                data.add(i + 1, Map.entry(iKey - kKey, -1));
                                k++;
                            }
                            break;
                        }
                    }
                }
//                System.out.format("k=%d, data=%s%n", k, data);
            }
        }
        System.out.println(data);

        long result = 0;
        for(int counter = 0, i = 0; i < data.size(); i++) {
            var entry = data.get(i);
            var key = entry.getKey();
            var value = entry.getValue();
            if(value > 0) for(int j = 0; j < key; j++, counter++) result += counter * value;
            else counter += key;
        }
        System.out.println(result);
    }
}
