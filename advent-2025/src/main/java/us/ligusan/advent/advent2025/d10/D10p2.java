package us.ligusan.advent.advent2025.d10;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D10p2 {
    static void main() {
//        var filename = "testInput.txt";
        var filename = "input.txt";

        var counter = new AtomicInteger();
        try (var scanner = new Scanner(D10p2.class.getResourceAsStream(filename))) {
            scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).forEach(s -> {
                Matcher m = Pattern.compile("\\{(.+)}").matcher(s);
                m.find();
                var target = Arrays.stream(m.group(1).split(",")).map(Integer::parseInt).toList();
                var targetSize = target.size();
                System.out.printf("target=%s%n", target);

                var cs = new ArrayList<List<Integer>>();
                var targets = Stream.iterate(0, i -> i < targetSize, i -> i + 1).map(_ -> new ArrayList<Integer>()).toList();
                for (m = Pattern.compile("\\(([^\\)]+)\\)").matcher(s); m.find(); ) {
                    var g = m.group(1);
                    System.out.println(g);

                    var c = Arrays.stream(g.split(",")).map(Integer::parseInt).toList();
                    c.forEach(i -> targets.get(i).add(cs.size()));
                    cs.add(c);
                }
                var xsSize = cs.size();
                System.out.printf("cs=%s, targets=%s%n", cs, targets);

                Integer min = null;

                var order = Stream.iterate(0, i -> i < xsSize, i -> i + 1).map(i -> Map.entry(i, cs.get(i).size())).sorted(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).toList();
                System.out.printf("order=%s%n", order);

                var digits = new ArrayList<Integer>(Collections.nCopies(xsSize, null));
                var calculatedDigits = new ArrayList<>(Collections.nCopies(xsSize, false));
                var i = xsSize - 1;
                var n = 0;
                for (long li = 0; ; i = 0) {
                    for (; 0 <= i && i < xsSize; i++, li++) {
                        Integer newDigit = null;

                        var ci = order.get(i);
                        var maxDigit = Stream.iterate(0, k -> k < targetSize, k -> k + 1).filter(k -> targets.get(k).contains(ci)).mapToInt(k -> target.get(k) - targets.get(k).stream().mapToInt(t -> {
                            var td = digits.get(t);
                            return td == null || t.equals(ci) ? 0 : td;
                        }).sum()).min().getAsInt();

                        var currentDigit = digits.get(ci);
                        if (currentDigit == null) {
                            var newDigits = Stream.iterate(0, j -> j < targetSize, j -> j + 1).filter(j -> {
                                var t = targets.get(j);
                                return t.contains(ci) && t.stream().allMatch(b -> b.equals(ci) || digits.get(b) != null);
                            }).map(j -> target.get(j) - targets.get(j).stream().mapToInt(b -> b.equals(ci) ? 0 : digits.get(b)).sum()).collect(Collectors.toSet());

                            if (newDigits.size() < 2) {
                                var calculate = !newDigits.isEmpty();
                                newDigit = calculate ? newDigits.iterator().next() : min == null ? maxDigit : Math.min(maxDigit, min - n - 1);
                                if (calculate && (newDigit < 0 || newDigit > maxDigit || min != null && n + newDigit >= min))
                                    newDigit = null;
                                if (newDigit != null) calculatedDigits.set(ci, calculate);
                            }
                        } else {
                            n -= currentDigit;
                            if (!calculatedDigits.get(ci) && currentDigit > 0) newDigit = currentDigit - 1;
                        }

                        digits.set(ci, newDigit);

                        if (li % 100_000_000 == 0)
                            System.out.printf("li=%d, i=%d, ci=%d, newDigit=%s, digits=%s, maxDigit=%d, calculatedDigits=%s%n", li, i, ci, newDigit, digits, maxDigit, calculatedDigits);

                        if (newDigit != null) {
                            n += newDigit;
                            i -= 2;
                        }
                    }
                    if (i >= xsSize) break;

                    if ((min == null || min > n) && Stream.iterate(0, j -> j < targetSize, j -> j + 1).allMatch(j -> targets.get(j).stream().mapToInt(digits::get).sum() == target.get(j))) {
                        min = n;
                        System.out.printf("%d: %s -> %d%n", li, digits, n);
                    }
                }
                System.out.printf("min=%d%n", min);
                counter.addAndGet(min);
            });
        }
        System.out.println(counter);
    }
}
