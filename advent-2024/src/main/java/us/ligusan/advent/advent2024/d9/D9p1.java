package us.ligusan.advent.advent2024.d9;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class D9p1 {
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
        for(int i = taken.size() - 1, freeIndex = 0, freePos = -1, freeValue = 0; i >= 0; i--) {
            var takenEntry = taken.get(i);
            for(int j = takenEntry.getValue(); j > 0; j--) {
                var takenPos = takenEntry.getKey() + j - 1;

                if(freePos < 0) {
                    var freeEntry = free.get(freeIndex);
                    freePos = freeEntry.getKey();
                    freeValue = freeEntry.getValue();
                }

                var replaceFlag = takenPos > freePos;
                result += (replaceFlag ? freePos : takenPos) * i;

//                System.out.format("i=%d, j=%d, takenPos=%d, freePos=%d, result=%d%n", i, j, takenPos, freePos, result);

                if(replaceFlag) if(--freeValue == 0) {
                    freeIndex++;
                    freePos = -1;
                }
                else freePos++;
            }
        }
        System.out.println(result);
    }
}
