package us.ligusan.advent.advent2025.d10;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D10p1 {
    static void main() {
        var filename = "input.txt";

        var counter = new AtomicInteger();
        try (var scanner = new Scanner(D10p1.class.getResourceAsStream(filename))) {
            scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).forEach(s -> {
                Matcher m = Pattern.compile("\\[(.+)]").matcher(s);
                m.find();

                var n = Integer.parseInt(new StringBuilder(m.group(1).replace('.', '0').replace('#', '1')).reverse().toString(), 2);
                System.out.println(n);

                var xs = new ArrayList<Integer>();
                for (m = Pattern.compile("\\(([^\\)]+)\\)").matcher(s); m.find(); ) {
                    var g = m.group(1);
                    System.out.println(g);
                    xs.add(Arrays.stream(g.split(",")).mapToInt(x -> (int) Math.pow(2, Integer.parseInt(x))).sum());
                }
                System.out.println(xs);

                var cache = new HashSet<Integer>();
                cache.add(0);
                Set<Integer> ns = new HashSet<>();
                ns.add(0);
                int i = 1;
                for (; ; i++) {
                    System.out.printf("%d: %s%n", i, ns);
                    ns = ns.stream().flatMap(t -> xs.stream().map(x -> t ^ x)).collect(Collectors.toSet());
                    if (ns.contains(n)) break;
                    ns.removeAll(cache);
                }

                System.out.println(i);

                counter.addAndGet(i);
            });
            
            System.out.println(counter);
        }
    }
}
