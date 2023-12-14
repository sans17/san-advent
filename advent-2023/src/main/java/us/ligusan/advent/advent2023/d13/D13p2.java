package us.ligusan.advent.advent2023.d13;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D13p2 {
    public static void main(final String[] args) {
        final var data = new ArrayList<List<Character>>();

        final var result = new AtomicInteger();
        try (var scanner = new Scanner(D13p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                if (s.isBlank()) {
                    result.addAndGet(summary(data));
                    System.out.format("result=%d\n", result.get());

                    data.clear();
                } else
                    data.add(s.chars().mapToObj(i -> (char) i).collect(Collectors.toList()));
            });
        }

        result.addAndGet(summary(data));
        System.out.format("result=%d\n", result.get());
    }

    private static boolean equalRows(final List<List<Character>> data, final int i, final int diff) {
        final var row1 = data.get(i - diff);
        final var row2 = data.get(i + diff + 1);

        final var ret = row1.equals(row2);
        System.out.format("i=%d, diff=%d, row1(%d)=%s, row2(%d)=%s, ret=%s\n", i, diff, i - diff, row1, i + diff + 1, row2, ret);

        return ret;
    }

    private static int summary(final List<List<Character>> data) {
        data.forEach(list -> {
            list.forEach(c -> System.out.format("%c", c));
            System.out.println();
        });
        System.out.format("-----\n");

        final var sizeY = data.size();
        final var sizeX = data.get(0).size();

        for (int smudgeY = 0 ; smudgeY < sizeY; smudgeY ++)
            for (int smudgeX = 0; smudgeX < sizeX; smudgeX++) {
                System.out.format("smudgeX=%d, smudgeY=%d\n", smudgeX, smudgeY);

                final var smudgeRow = data.get(smudgeY);
                final var c = smudgeRow.get(smudgeX);
                smudgeRow.set(smudgeX, c == '.' ? '#' : '.');

                try {
                    Integer y = null;
                    for (int i = smudgeY; i < sizeY - 1; i++) {
                        if (!(smudgeY >= i - Math.min(i + 1, sizeY - i - 1) + 1 && smudgeY <= i)) continue;
                        if (!equalRows(data, i, i - smudgeY)) continue;

                        y = i;
                        for (int j = 0; i - j >= 0 && i + j + 1 < sizeY; j++)
                            if (!equalRows(data, i, j)) {
                                y = null;
                                break;
                            }
                        if (y != null) break;
                    }
                    if (y != null) {
                        System.out.format("y=%d\n", y);
                        System.out.format("smudgeX=%d, smudgeY=%d\n", smudgeX, smudgeY);
                        return (y + 1) * 100;
                    }

                    var dataTransposed = Stream.iterate(0, i -> i < sizeX, i -> i + 1).map(i -> Stream.iterate(0, j -> j < sizeY, j -> j + 1).map(j -> data.get(j).get(i)).collect(Collectors.toList())).collect(Collectors.toList());

//        System.out.format("dataTransposed=%s\n", dataTransposed);

                    Integer x = null;
                    for (int i = smudgeX; i < sizeX - 1; i++) {
                        if (!(smudgeX >= i - Math.min(i + 1, sizeX - i - 1) + 1 && smudgeX <= i)) continue;
                        if (!equalRows(dataTransposed, i, i - smudgeX)) continue;

                        x = i;
                        for (int j = 0; i - j >= 0 && i + j + 1 < sizeX; j++) {
//                System.out.format("i=%d, j=%d\n", i, j);
//                System.out.format("i=%d, j=%d, dataTransposed.get(i - j)(%d)=%s, dataTransposed.get(i + j + 1)(%d)=%s\n", i, j, i - j, dataTransposed.get(i - j), i + j + 1, dataTransposed.get(i + j + 1));

                            if (!equalRows(dataTransposed, i, j)) {
                                x = null;
                                break;
                            }
                        }
                        if (x != null) break;
                    }

                    if (x != null) return x + 1;
                } finally {
                    smudgeRow.set(smudgeX, c);
                }
            }

        System.out.format("ret=0\n");
        return 0;
    }
}
