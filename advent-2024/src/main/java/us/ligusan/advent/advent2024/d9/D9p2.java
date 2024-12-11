package us.ligusan.advent.advent2024.d9;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class D9p2 {
    public static void main(final String[] args) throws Exception {
        var taken = new ArrayList<Map.Entry<Integer, Integer>>();
        var free = new ArrayList<Map.Entry<Integer, Integer>>();

        try(var scanner = new Scanner(D9p1.class.getResourceAsStream("input.txt"))) {
            var indexRef = new AtomicInteger();
            var counterRef = new AtomicInteger();
            scanner.nextLine().chars().forEach(c -> {
                var value = c - '0';
                var takenFlag = indexRef.getAndIncrement() % 2 == 0;
                if(takenFlag || value > 0) (takenFlag ? taken : free).add(Map.entry(counterRef.getAndAdd(value), value));
            });
        }
        System.out.println(taken);
        System.out.println(free);

        var result = 0L;
        for(int i = taken.size() - 1; i >= 0; i--) {
            var takenEntry = taken.get(i);
            var takenKey = takenEntry.getKey();
            var takenValue = takenEntry.getValue();

            var freeIndex = -1;
            var freeKey = 0;
            var freeValue = 0;
            for(int j = 0; j < free.size(); j++) {
                var freeEntry = free.get(j);
                freeKey = freeEntry.getKey();
                freeValue = freeEntry.getValue();
                if(freeKey > takenKey) {
                    break;
                }
                if(freeValue >= takenValue) {
                    freeIndex = j;
                    break;
                }
            }

            for(int k = 0; k < takenValue; k++) result += ((freeIndex < 0 ? takenKey : freeKey) + k) * i;

            if(freeIndex >= 0) if(freeValue > takenValue) free.set(freeIndex, Map.entry(freeKey + takenValue, freeValue - takenValue));
            else free.remove(freeIndex);

//            System.out.format("i=%d, freeIndex=%d, result=%d, free=%s%n", i, freeIndex, result, free);
        }
        System.out.println(result);
    }
}
