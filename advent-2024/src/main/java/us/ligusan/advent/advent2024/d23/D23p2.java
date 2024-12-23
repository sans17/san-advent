package us.ligusan.advent.advent2024.d23;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D23p2 {
    public static void main(final String[] args) {
//        var file = "testInput.txt";
        var file = "input.txt";

        var data = new HashMap<String, Set<String>>();

        try(var scanner = new Scanner(D23p2.class.getResourceAsStream(file))) {
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

        AtomicReference<Set<String>> maxRef = new AtomicReference<>();
        for(var connectionsSet = data.keySet().stream().map(Set::of).collect(Collectors.toSet()); !connectionsSet.isEmpty();
         connectionsSet = connectionsSet.stream().flatMap(cs -> data.entrySet().stream().filter(e -> !cs.contains(e.getKey()) && e.getValue().containsAll(cs)).map(e -> {
             var ret = new HashSet<>(cs);
             ret.add(e.getKey());

             var max = maxRef.get();
             if(max == null || ret.size() > max.size()) maxRef.set(ret);

             return ret;
         })).collect(Collectors.toSet()))
            ;

        System.out.println(String.join(",", new TreeSet<>(maxRef.get())));
    }
}
