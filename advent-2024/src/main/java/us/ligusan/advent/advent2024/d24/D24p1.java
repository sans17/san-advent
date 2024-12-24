package us.ligusan.advent.advent2024.d24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D24p1 {
    public static void main(final String[] args) throws Exception {
//        var file = "testInput.txt";
//        var file = "testInput1.txt";
        var file = "input.txt";

        var valuesMap = new HashMap<String, Boolean>();
        var rulesMap = new HashMap<Map.Entry<String, String>, List<Map.Entry<String, String>>>();

        boolean instructionsFlag = false;

        try(var scanner = new Scanner(D24p1.class.getResourceAsStream(file))) {
            for(String s; scanner.hasNextLine(); ) {
                s = scanner.nextLine();
                System.out.println(s);

                if(instructionsFlag) {
                    var m = Pattern.compile("([a-z\\d]+) (X?OR|AND) ([a-z\\d]+) -> ([a-z\\d]+)").matcher(s);
                    m.find();
                    rulesMap.computeIfAbsent(Map.entry(m.group(1), m.group(3)), _ -> new ArrayList<>()).add(Map.entry(m.group(2), m.group(4)));
                }
                else if(s.isEmpty()) {
                    instructionsFlag = true;
                    System.out.format("valuesMap=%s%n", valuesMap);
                }
                else {
                    var m = Pattern.compile("([a-z\\d]+): ([01])").matcher(s);
                    m.find();
                    valuesMap.put(m.group(1), "1".equals(m.group(2)));
                }
            }
        }
        System.out.format("rulesMap=%s%n", rulesMap);

        var zs = rulesMap.entrySet().stream().flatMap(e -> {
            var key = e.getKey();
            return Stream.concat(Stream.of(key.getKey(), key.getValue()), e.getValue().stream().map(Map.Entry::getValue));
        }).filter(s -> s.charAt(0) == 'z').collect(Collectors.toSet());
        System.out.format("zs=%s%n", zs);

        for(var i = 0; !valuesMap.keySet().containsAll(zs); i++) {
            for(var e : rulesMap.entrySet()) {
                var operands = e.getKey();
                var rulesList = e.getValue();
                for(var rule : rulesList) {
                    var output = rule.getValue();
                    if(!valuesMap.containsKey(output)) {
                        var left = valuesMap.get(operands.getKey());
                        var right = valuesMap.get(operands.getValue());
                        if(left != null && right != null) {
                            var value = switch(rule.getKey()) {
                                case "AND" -> left && right;
                                case "OR" -> left || right;
                                case "XOR" -> left ^ right;
                                default -> throw new RuntimeException("Unknown operator");
                            };
                            System.out.format("e=%s, value=%s%n", e, value);
                            valuesMap.put(output, value);
                        }
                    }
                }
            }
            System.out.format("i=%d, valuesMap=%s%n", i, valuesMap);
        }

        var sorted = valuesMap.entrySet().stream().filter(e -> e.getKey().charAt(0) == 'z').sorted(Map.Entry.<String, Boolean> comparingByKey().reversed()).toList();
        System.out.println(sorted);
        System.out.println(Long.parseLong(sorted.stream().map(e -> e.getValue() ? "1" : "0").collect(Collectors.joining()), 2));
    }
}
