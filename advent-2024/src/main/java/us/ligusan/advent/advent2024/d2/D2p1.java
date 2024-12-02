package us.ligusan.advent.advent2024.d2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class D2p1 {
    public static void main(final String[] args) {
        final var matrix = new ArrayList<List<Integer>>();

        try (var scanner = new Scanner(D2p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                final var row = new ArrayList<Integer>();
                matrix.add(row);

                Pattern.compile("(\\d+)").matcher(s).results().forEach(m -> row.add(Integer.parseInt(m.group(1))));
            });
        }
        System.out.format("matrix=%s%n", matrix);

        var counter = 0;
        for (final var row : matrix) {
            counter++;

            Integer direction = null;
            for (int i = 0; i < row.size() - 1; i++) {
                var diff = row.get(i) - row.get(i + 1);
                if(diff == 0) {
                    counter--;
                    break;
                }
                if (direction == null) direction = (int)Math.signum(diff);
                if (Math.abs(diff) > 3 || direction * diff < 0) {
                    counter--;
                    break;
                }
            }
        }
        System.out.println(counter);
    }
}
