package us.ligusan.advent.advent2024.d23;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D23p1 {
    public static void main(final String[] args) {
//        var file = "testInput.txt";
        var file = "input.txt";

        var data = new HashMap<String, Set<String>>();

        try(var scanner = new Scanner(D23p1.class.getResourceAsStream(file))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                var m = Pattern.compile("([a-z]+)-([a-z]+)").matcher(s);
                m.find();
                var c1 = m.group(1);
                var c2 = m.group(2);
                data.computeIfAbsent(c1, _ -> new HashSet<>()).add(c2);
                data.computeIfAbsent(c2, _ -> new HashSet<>()).add(c1);
            });
        }
        System.out.format("data=%s%n", data);

        System.out.println(data.entrySet().stream().filter(e -> e.getKey().charAt(0) == 't').flatMap(e -> {
            var c1 = e.getKey();
            var s1 = e.getValue();
            return s1.stream().flatMap(c2 -> {
                var s1copy = new HashSet<>(s1);
                s1copy.retainAll(data.get(c2));
                return s1copy.stream().map(c3 -> Set.of(c1, c2, c3));
            });
        }).collect(Collectors.toSet()).size());
    }
}
