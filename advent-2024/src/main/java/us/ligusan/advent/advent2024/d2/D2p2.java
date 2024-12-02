package us.ligusan.advent.advent2024.d2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class D2p2 {
    public static void main(final String[] args) {
        final var matrix = new ArrayList<List<Integer>>();

        try (var scanner = new Scanner(D2p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                final var row = new ArrayList<Integer>();
                matrix.add(row);

                Pattern.compile("(\\d+)").matcher(s).results().forEach(m -> row.add(Integer.parseInt(m.group(1))));
            });
        }
        System.out.format("matrix=%s%n", matrix);
        System.out.format("matrix.size=%d%n", matrix.size());

        var counter = 0;
        for(final var row : matrix) {
            boolean errorFlag = false;
            var errorIndex = testRow(row);
            if(errorFlag = errorIndex != null) for(int i = 0; i < row.size(); i++) {
                var testRow = new ArrayList<>(row);
                testRow.remove(i);
                if(!(errorFlag = testRow(testRow) != null)) break;
            }
            if(!errorFlag) counter++;
        }
        System.out.println(counter);
    }

    private static Integer testRow(final List<Integer> row) {
        Integer direction = null;
        for(int i = 0; i < row.size() - 1; i++) {
            var diff = row.get(i) - row.get(i + 1);
            if(diff == 0) return i;
            if(direction == null) direction = (int)Math.signum(diff);
            if(Math.abs(diff) > 3 || direction * diff < 0) return i;
        }
        return null;
    }
}
