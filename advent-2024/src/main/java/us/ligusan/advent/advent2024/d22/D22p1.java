package us.ligusan.advent.advent2024.d22;

import java.util.Scanner;
import java.util.stream.Collectors;

public class D22p1 {
    private static final int LIMIT = 16_777_216;

    public static void main(final String[] args) throws Exception {
//        var file = "testInput1.txt";
//        var interations = 10;
//        var file = "testInput.txt";
        var file = "input.txt";
        var interations = 2_000;

        try (var scanner = new Scanner(D22p1.class.getResourceAsStream(file))) {
            var data = scanner.useDelimiter("\r?\n").tokens().map(Integer::parseInt).collect(Collectors.toList());
            System.out.println(data);

            for (int i = 0; i < interations; i++) {
                for (int j = 0; j < data.size(); j++) {
                    long d = data.get(j);
                    d = ((d * 64) ^ d) % LIMIT;
                    d = ((d / 32) ^ d) % LIMIT;
                    d = ((d * 2048) ^ d) % LIMIT;
                    data.set(j, (int) d);
                }
//                System.out.println(data);
            }

            System.out.println(data.stream().mapToLong(i -> (long) i).sum());
        }
    }
}
