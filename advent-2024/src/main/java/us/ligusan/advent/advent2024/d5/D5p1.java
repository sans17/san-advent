package us.ligusan.advent.advent2024.d5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class D5p1 {
    public static void main(final String[] args) throws Exception {
        var rules = new ArrayList<List<Integer>>();
        var lists = new ArrayList<List<Integer>>();

        var rulesFlagRef = new AtomicBoolean(true);
        try(var scanner = new Scanner(D5p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s%n", s);

                if(s.isBlank()) rulesFlagRef.set(false);
                else {
                    var rulesFlag = rulesFlagRef.get();
                    (rulesFlag ? rules : lists).add(Arrays.stream(s.split(rulesFlag ? "\\|" : ",")).map(Integer::parseInt).toList());
                }
            });
        }
        System.out.format("rules=%s%n", rules);
        System.out.format("lists=%s%n", lists);

        var result = lists.stream().filter(list -> rules.stream().noneMatch(rule -> {
            var i = list.indexOf(rule.get(0));
            if(i >= 0) {
                var j = list.indexOf(rule.get(1));
                if(j >= 0) return j < i;
            }
            return false;
        })).mapToInt(list -> list.get((list.size() - 1) / 2)).sum();
        System.out.println(result);
    }
}
