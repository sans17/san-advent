package us.ligusan.advent.advent2023.d24;

import us.ligusan.advent.advent2023.Point;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D24p1 {
    public static void main(final String[] args) {
        final var inputFileName = "input.txt";
        final var min = new BigDecimal("200000000000000");
        final var max = new BigDecimal("400000000000000");

//        final var inputFileName = "testInput.txt";
//        final var min = new BigDecimal(7);
//        final var max = new BigDecimal(27);

        List<Line> data;

        try (var scanner = new Scanner(D24p1.class.getResourceAsStream(inputFileName))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.format("s=%s\n", s);
                final var matchResult = Pattern.compile("(\\d+), +(\\d+), +(\\d+) @ +(-?\\d+), +(-?\\d+), +(-?\\d+)").matcher(s).results().findFirst().get();
                return new Line(new PointBig(new BigInteger(matchResult.group(1)), new BigInteger(matchResult.group(2))), new Point(Integer.valueOf(matchResult.group(4)), Integer.valueOf(matchResult.group(5))));
            }).collect(Collectors.toList());
        }
        System.out.println(data);

        final var size = data.size();

        int counter = 0;
        for (int i = 0; i < size - 1; i++)
            for (int j = i + 1; j < size; j++) {
//                System.out.println();
//                System.out.format("i=%d, j=%d: ", i, j);

                final var line1 = data.get(i);
                final var line2 = data.get(j);

//                System.out.format("line1=%s, line2=%s\n", line1, line2);

                final var x1 = line1.p().x();
                final var y1 = line1.p().y();
                final var dx1 = line1.v().x();
                final var dy1 = line1.v().y();

                final var x2 = line2.p().x();
                final var y2 = line2.p().y();
                final var dx2 = line2.v().x();
                final var dy2 = line2.v().y();

                final var divisor = dx1 * dy2 - dx2 * dy1;
                if (divisor == 0) {
//                    System.out.format("divisor=%s\n", divisor);
                    continue;
                }

                final var y = new BigDecimal(x2.subtract(x1).multiply(BigInteger.valueOf(dy1 * dy2)).subtract(y2.multiply(BigInteger.valueOf(dx2 * dy1))).add(y1.multiply(BigInteger.valueOf(dx1 * dy2)))).divide(new BigDecimal(divisor), 2, RoundingMode.HALF_UP);
//                System.out.format("y=%s ", y);
                if (min.compareTo(y) > 0 || y.compareTo(max) > 0) continue;

                final var t1 = y.subtract(new BigDecimal(y1)).divide(new BigDecimal(dy1), 2, RoundingMode.HALF_UP);
//                System.out.format("t1=%s ", t1);
                if (t1.compareTo(BigDecimal.ZERO) < 0) continue;
                final var t2 = y.subtract(new BigDecimal(y2)).divide(new BigDecimal(dy2), 2, RoundingMode.HALF_UP);
//                System.out.format("t2=%s ", t2);
                if (t2.compareTo(BigDecimal.ZERO) < 0) continue;

                final var x = new BigDecimal(x1).add(new BigDecimal(dx1).multiply(t1));
//                System.out.format("x=%s ", x);
                if (min.compareTo(x) > 0 || x.compareTo(max) > 0) continue;

                counter++;

                System.out.println();
                System.out.format("i=%d, j=%d: ", i, j);
                System.out.format("line1=%s, line2=%s\n", line1, line2);
                System.out.format("divisor=%s\n", divisor);
                System.out.format("y=%s, ", y);
                System.out.format("t1=%s, ", t1);
                System.out.format("t2=%s, ", t2);
                System.out.format("x=%s\n", x);

                System.out.format("counter=%d\n", counter);
            }

        System.out.println();
        System.out.println(counter);
    }
}

record PointBig(BigInteger x, BigInteger y) {
}

record Line(PointBig p, Point v) {
}


