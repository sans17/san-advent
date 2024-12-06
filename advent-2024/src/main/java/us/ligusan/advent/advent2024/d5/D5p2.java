package us.ligusan.advent.advent2024.d5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class D5p2 {
    public static void main(final String[] args) throws Exception {
        var rules = new ArrayList<List<Integer>>();
        var lists = new ArrayList<List<Integer>>();

        var rulesFlagRef = new AtomicBoolean(true);
        try(var scanner = new Scanner(D5p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s%n", s);

                if(s.isBlank()) rulesFlagRef.set(false);
                else {
                    var rulesFlag = rulesFlagRef.get();
                    (rulesFlag ? rules : lists).add(
                     Arrays.stream(s.split(rulesFlag ? "\\|" : ",")).map(Integer::parseInt)
                      .collect(Collectors.toList()));
                }
            });
        }
        System.out.format("rules=%s%n", rules);
        System.out.format("lists=%s%n", lists);

        var result = lists.stream().filter(list -> rules.stream().anyMatch(rule -> {
            var i = list.indexOf(rule.get(0));
            if(i >= 0) {
                var j = list.indexOf(rule.get(1));
                if(j >= 0) return j < i;
            }
            return false;
        })).map(list -> {
            System.out.format("list=%s%n", list);

            var fixedList = new ArrayList<>(list);
            for(var fixed = false; !fixed; ) {
                fixed = true;
                for(var rule : rules) {
                    var left = rule.get(0);
                    var i = fixedList.indexOf(left);
                    if(i >= 0) {
                        var right = rule.get(1);
                        var j = fixedList.indexOf(right);
                        if(j >= 0 && j < i) {
                            fixed = false;
                            fixedList.set(j, left);
                            fixedList.set(i, right);

                            System.out.format("rule=%s, fixedList=%s%n", rule, fixedList);
                            break;
                        }
                    }
                }
            }

            System.out.format("fixedList=%s%n%n", fixedList);
            return fixedList;
        }).mapToInt(list -> list.get((list.size() - 1) / 2)).sum();
        System.out.println(result);
    }
}
