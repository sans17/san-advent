package us.ligusan.advent.advent2023.d11;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D11p2 {
    public static void main(final String[] args) {
        final List<List<Character>> data;

        try (var scanner = new Scanner(D11p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList());
        }

        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });

        final var expandingRows = new ArrayList<Integer>();
        final var expandingColumns = new ArrayList<Integer>();

        Stream.iterate(0, y -> y < data.size(), y -> y + 1).forEach(y -> {
            final var list = data.get(y);
            if (list.stream().allMatch(c -> c == '.')) expandingRows.add(y);
        });

        Stream.iterate(0, x -> x < data.get(0).size(), x -> x + 1).forEach(x -> {
            if(Stream.iterate(0, y -> y < data.size(), y -> y + 1).map(y -> data.get(y).get(x)).allMatch(c -> c == '.')) expandingColumns.add(x);
        });

        System.out.format("expandingRows=%s\n", expandingRows);
        System.out.format("expandingColumns=%s\n", expandingColumns);

        final var galaxies = Stream.iterate(0, y -> y < data.size(), y -> y + 1).flatMap(y -> Stream.iterate(0, x -> x < data.get(0).size(), x -> x + 1).map(x -> Map.entry(x, y)).filter(entry -> data.get(entry.getValue()).get(entry.getKey()) == '#')).collect(Collectors.toList());
        System.out.format("galaxies=%s\n", galaxies);

        var ret = BigInteger.ZERO;
        var expand = 1_000_000 - 1;
        final var size = galaxies.size();
        for (int i = 0; i < size; i++)
            for (int j = i + 1; j < size; j++) {
                final var galaxy1 = galaxies.get(i);
                final var galaxy2 = galaxies.get(j);

                final var x1 = galaxy1.getKey();
                final var y1 = galaxy1.getValue();
                final var x2 = galaxy2.getKey();
                final var y2 = galaxy2.getValue();

                final var minX = Math.min(x1, x2);
                final var maxX = Math.max(x1, x2);
                final var minY = Math.min(y1, y2);
                final var maxY = Math.max(y1, y2);

                ret = ret.add(BigInteger.valueOf(maxX - minX + expand * expandingColumns.stream().filter(x -> x > minX && x < maxX).count() + maxY - minY + expand * expandingRows.stream().filter(y -> y > minY && y < maxY).count()));
            }

        System.out.println(ret);
    }
}
