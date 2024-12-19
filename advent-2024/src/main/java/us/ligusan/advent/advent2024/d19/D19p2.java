package us.ligusan.advent.advent2024.d19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class D19p2 {
    private static final Map<String, Long> FOUND = new HashMap<>();

    private static List<String> towels;

    public static void main(final String[] args) throws Exception {

        try(var scanner = new Scanner(D19p2.class.getResourceAsStream("input.txt"))) {
            var s = scanner.nextLine();
            System.out.println(s);
            towels = Arrays.asList(s.split(",\\s+"));
            System.out.println(towels);

            scanner.nextLine();

            var counter = 0L;
            while(scanner.hasNextLine()) {
                s = scanner.nextLine();
                System.out.println(s);

                var result = find(s);
                System.out.format("result=%d%n", result);
                counter += result;
            }
            System.out.println(counter);
        }
    }

    private static long find(String pattern) {
        Long ret = FOUND.get(pattern);
        if(ret != null) return ret;

        ret = 0L;
        for(var towel : towels)
            if(pattern.startsWith(towel)) {
                var sub = pattern.substring(towel.length());
                ret += sub.isEmpty() ? 1 : find(sub);
            }
        FOUND.put(pattern, ret);
        return ret;
    }
}
