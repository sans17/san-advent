package us.ligusan.advent.advent2024.d1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class D1p1 {
    public static void main(final String[] args) {
        final var left = new ArrayList<Integer>();
        final var right = new ArrayList<Integer>();

        try (var scanner = new Scanner(D1p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                Pattern.compile("(\\d+)\\s+(\\d+)").matcher(s).results().forEach(m -> {
                    left.add(Integer.parseInt(m.group(1)));
                    right.add(Integer.parseInt(m.group(2)));
                });
            });
        }
        System.out.format("left=%s%nright=%s%n", left, right);

        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        var counter = 0;
        for(int i = 0; i < left.size(); i++) counter += Math.abs(left.get(i) - right.get(i));
        System.out.println(counter);
    }
}
