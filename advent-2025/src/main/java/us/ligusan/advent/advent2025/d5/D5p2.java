package us.ligusan.advent.advent2025.d5;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D5p2 {
    static void main() {
        var ranges = new ArrayList<List<Long>>();

        try (var scanner = new Scanner(D5p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n");
            while (scanner.hasNext()) {
                var s = scanner.next();
                if (s.isBlank()) break;

                Matcher m = Pattern.compile("(\\d+)-(\\d+)").matcher(s);
                m.find();

                ranges.add(List.of(Long.parseLong(m.group(1)), Long.parseLong(m.group(2))));
            }
        }

        outer:
        for (int i = 0; i < ranges.size(); ) {
            var range1 = ranges.get(i);
            System.out.printf("%d: %s%n", i, range1);

            var bottom1 = range1.get(0);
            var top1 = range1.get(1);
            for (int j = i + 1; j < ranges.size(); j++) {
                var range2 = ranges.get(j);
                var bottom2 = range2.get(0);
                var top2 = range2.get(1);
                if (bottom1 < bottom2 ? top1 >= bottom2 : top2 >= bottom1) {
                    ranges.remove(j);
                    ranges.set(i, List.of(Math.min(bottom1, bottom2), Math.max(top1, top2)));

                    System.out.printf("\t%d: %s%n", j, range2);
                    continue outer;
                }
            }
            i++;
        }

        System.out.println(ranges.stream().mapToLong(l -> {
            var ret = l.get(1) - l.getFirst() + 1;
            System.out.printf("%s: %d%n", l, ret);
            return ret;
        }).sum());
    }
}
