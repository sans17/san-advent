package us.ligusan.advent.advent2024.d24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class D24p2 {
    public static void main(final String[] args) throws Exception {
//        var file = "testInput.txt";
//        var file = "testInput1.txt";
//        var file = "input.txt";
        var file = "correctedInput.txt";

        var rulesMap = new HashMap<Set<String>, List<Map.Entry<String, String>>>();

        boolean instructionsFlag = false;

        var result = new ArrayList<String>();

        try(var scanner = new Scanner(D24p2.class.getResourceAsStream(file))) {
            for(String s; scanner.hasNextLine(); ) {
                s = scanner.nextLine();
                System.out.println(s);

                if(instructionsFlag) if(s.charAt(0) == '#') {
                    var m = Pattern.compile("#([a-z\\d]+) (X?OR|AND) ([a-z\\d]+) -> ([a-z\\d]+)").matcher(s);
                    m.find();
                    result.add(m.group(4));
                }
                else {
                    var m = Pattern.compile("([a-z\\d]+) (X?OR|AND) ([a-z\\d]+) -> ([a-z\\d]+)").matcher(s);
                    m.find();
                    rulesMap.computeIfAbsent(Set.of(m.group(1), m.group(3)), _ -> new ArrayList<>()).add(Map.entry(m.group(2), m.group(4)));
                }
                else if(s.isEmpty()) instructionsFlag = true;
            }
        }
        System.out.format("rulesMap=%s%n", rulesMap);

        var xors = new ArrayList<String>();
        var ands = new ArrayList<String>();
        for(var i = 0; i < 45; i++) {
            var is = (i < 10 ? "0" : "") + i;
            for(var e : rulesMap.get(Set.of('x' + is, 'y' + is)))
                (switch(e.getKey()) {
                    case "XOR" -> xors;
                    case "AND" -> ands;
                    default -> throw new RuntimeException("Unknown operation: " + e.getKey());
                }).add(e.getValue());
        }
        System.out.format("xors=%s%n", xors);
        System.out.format("ands=%s%n", ands);

        var overflows = new ArrayList<String>();
        overflows.add(ands.getFirst());

        for(var i = 1; i < 45; i++) {
            System.out.format("i=%d%n", i);
            var is = (i < 10 ? "0" : "") + i;

            var key = Set.of(xors.get(i), overflows.get(i - 1));
            var a = rulesMap.get(key);
            if(a == null) {
                System.out.format("0: key=%s, a=%s%n", key, a);
                break;
            }
            if(!a.contains(Map.entry("XOR", 'z' + is))) {
                System.out.format("1: key=%s, a=%s%n", key, a);
                break;
            }
            var l = a.stream().filter(e -> "AND".equals(e.getKey())).toList();
            if(l.size() != 1) {
                System.out.format("2: a=%s%n", a);
                break;
            }
            var ol = rulesMap.get(Set.of(l.getFirst().getValue(), ands.get(i)));
            var o = ol.stream().filter(e -> "OR".equals(e.getKey())).toList();
            if(o.size() != 1) {
                System.out.format("3: ol=%s%n", ol);
                break;
            }
            overflows.add(o.getFirst().getValue());
        }
        System.out.format("overflows=%s%n", overflows);

        result.sort(String::compareTo);
        System.out.println(String.join(",", result));
    }
}
