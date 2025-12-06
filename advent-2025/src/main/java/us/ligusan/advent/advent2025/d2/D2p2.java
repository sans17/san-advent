package us.ligusan.advent.advent2025.d2;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D2p2 {
    static void main() {
        AtomicLong result = new AtomicLong();

        try (var scanner = new Scanner(D2p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter(",").tokens().forEach(s -> {
                final String input = s.trim();
                System.out.println(input);

                Matcher m = Pattern.compile("(\\d+)-(\\d+)").matcher(input);
                m.find();

                final String g1 = m.group(1);
                final String g2 = m.group(2);
                final long topValue = Long.parseLong(g2);
                final Set<Long> foundValues = new HashSet<>();

                outer:
                for (int i = 1; ; i++) {
                    final String iString = Integer.toString(i);

                    boolean firstFlag = true;
                    for (String testString = iString + iString; ; testString += iString) {
                        final long testValue = Long.parseLong(testString);
                        if (testValue > topValue) if (firstFlag) break outer;
                        else break;
                        else if (!foundValues.contains(testValue) && Long.parseLong(g1) <= testValue) {
                            System.out.printf("\t%d%n", testValue);

                            foundValues.add(testValue);
                            result.addAndGet(testValue);
                        }
                        firstFlag = false;
                    }
                }
            });
        }

        System.out.printf("result=%s%n", result);
    }
}
