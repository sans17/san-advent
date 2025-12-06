package us.ligusan.advent.advent2025.d5;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D5p1 {
    static void main() {
        var ranges = new ArrayList<List<Long>>();

        var counter = new AtomicInteger();

        var rangesFlagRef = new AtomicBoolean(true);
        try (var scanner = new Scanner(D5p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                if (s.isBlank()) rangesFlagRef.set(false);
                else if (rangesFlagRef.get()) {
                    Matcher m = Pattern.compile("(\\d+)-(\\d+)").matcher(s);
                    m.find();

                    ranges.add(List.of(Long.parseLong(m.group(1)), Long.parseLong(m.group(2))));
                } else {
                    var id = Long.parseLong(s);
                    if (ranges.stream().anyMatch(range -> range.getFirst() <= id && id <= range.get(1)))
                        counter.incrementAndGet();
                }
            });
        }

        System.out.println(counter);
    }
}
