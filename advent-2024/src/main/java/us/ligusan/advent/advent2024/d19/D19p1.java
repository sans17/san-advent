package us.ligusan.advent.advent2024.d19;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class D19p1 {
    private static final Set<String> FOUND = new HashSet<>();
    private static final Set<String> IMPOSSIBLE = new HashSet<>();

    private static List<String> towels;

    public static void main(final String[] args) throws Exception {

        try(var scanner = new Scanner(D19p1.class.getResourceAsStream("input.txt"))) {
            var s = scanner.nextLine();
            System.out.println(s);
            towels = Arrays.asList(s.split(",\\s+"));
            System.out.println(towels);

            scanner.nextLine();

            var counter = 0;
            while(scanner.hasNextLine()) {
                s = scanner.nextLine();
                System.out.println(s);

                if(find(s)) counter++;
            }
            System.out.println(counter);
        }
    }

    private static boolean find(String pattern) {
        if(FOUND.contains(pattern)) return true;
        if(IMPOSSIBLE.contains(pattern)) return false;

        var ret = false;
        for(var towel : towels) if(pattern.startsWith(towel)) {
            var sub = pattern.substring(towel.length());
            if(ret = sub.isEmpty() || find(sub)) break;
        }
        (ret ? FOUND : IMPOSSIBLE).add(pattern);
        return ret;
    }
}
