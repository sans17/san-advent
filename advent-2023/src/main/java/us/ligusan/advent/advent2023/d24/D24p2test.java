package us.ligusan.advent.advent2023.d24;

import us.ligusan.advent.advent2023.Point;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D24p2test {
    private static final MathContext CONTEXT = new MathContext(20);
    private static final BigDecimal EPSILON = new BigDecimal(Math.pow(10, -CONTEXT.getPrecision()));

    public static void main(final String[] args) {
        final var inputFileName = "input.txt";
        final var maxCounter = 242;

//        final var inputFileName = "testInput.txt";
//        final var maxCounter = 4;

        List<Line3BigDecimal> data;

        try (var scanner = new Scanner(D24p1.class.getResourceAsStream(inputFileName))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.format("s=%s\n", s);
                final var matchResult = Pattern.compile("(\\d+), +(\\d+), +(\\d+) @ +(-?\\d+), +(-?\\d+), +(-?\\d+)").matcher(s).results().findFirst().get();
                return new Line3BigDecimal(new Point3BigDecimal(new BigDecimal(matchResult.group(1)), new BigDecimal(matchResult.group(2)), new BigDecimal(matchResult.group(3))), new Point3BigDecimal(new BigDecimal(matchResult.group(4)), new BigDecimal(matchResult.group(5)), new BigDecimal(matchResult.group(6))));
            }).collect(Collectors.toList());
        }
        System.out.println(data);

        final var size = data.size();

        System.out.format("data.size=%s\n", data.size());

//        int counterImax = 0;

        for (int i = 0; i < size; i++) {
            final var lineI = data.get(i);

            final var projectionMap = new HashMap<Integer, Line3BigDecimal>();

//            System.out.format("--- %d\n", i);
            for (int j = 0; j < size; j++) {
                if (i == j) continue;

                projectionMap.put(j, project(lineI, data.get(j)));
            }

//            int counterMax = 0;
//                BigDecimal t1Min = null;
//                BigDecimal t1Max = null;


            for (int k = 0; k < size; k++) {
                if (i == k) continue;

                final var line1 = projectionMap.get(k);

                final var x1 = line1.p().x();
                final var y1 = line1.p().y();
                final var dx1 = line1.v().x();
                final var dy1 = line1.v().y();

                int counter = 0;
                int parallelCounter = 0;
                for (int j = 0; j < size; j++) {
                    if (i == j || k == j) continue;

                    final var line2 = projectionMap.get(j);

                    final var x2 = line2.p().x();
                    final var y2 = line2.p().y();
                    final var dx2 = line2.v().x();
                    final var dy2 = line2.v().y();

                    final var divisor = dx1.multiply(dy2).subtract(dx2.multiply(dy1)).round(CONTEXT);
                    if (divisor.abs().compareTo(EPSILON) <= 0) {
                        parallelCounter++;
                        continue;
                    }

//                    System.out.format("divisor=%s\n", divisor.toPlainString());
                    final var y = x2.subtract(x1).multiply(dy1).multiply(dy2).subtract(y2.multiply(dx2).multiply(dy1)).add(y1.multiply(dx1).multiply(dy2)).divide(divisor, 2, RoundingMode.HALF_UP);
//                    if (min.compareTo(y) > 0 || y.compareTo(max) > 0) continue;

                    final var t1 = y.subtract(y1).divide(dy1, 2, RoundingMode.HALF_UP);
                    final var t2 = y.subtract(y2).divide(dy2, 2, RoundingMode.HALF_UP);

                    final var x = x1.add(dx1).multiply(t1);

//                    if (t1Min == null || t1Min.compareTo(t1) > 0) {
//                        t1Min = t1;
//                    }
//                    if (t1Max == null || t1Max.compareTo(t1) < 0) {
//                        t1Max = t1;
//                    }

                    if (t1.compareTo(BigDecimal.ZERO) < 0 || t2.compareTo(BigDecimal.ZERO) < 0) {
//                        System.out.format("%d, %d: x=%s, y=%s, t1=%s, t2=%s\n", k, j, x.toPlainString(), y.toPlainString(), t1.toPlainString(), t2.toPlainString());

                        counter++;
                    }
//                    if (min.compareTo(x) > 0 || x.compareTo(max) > 0) continue;

//                    System.out.format("%d, %d: x=%s, y=%s, t1=%s, t2=%s\n", k, j, x.toPlainString(), y.toPlainString(), t1.toPlainString(), t2.toPlainString());
                }
//                System.out.format("counter=%d\n", counter);
//                if (counter > counterMax) {
//                    counterMax = counter;
//                }

                if (counter == maxCounter) {
                    System.out.format("i=%d, k=%d: lineI=%s, line1=%s, data.get(%d)=%s\n", i, k, lineI, line1, k, data.get(k));
                    System.out.format("parallelCounter=%d\n", parallelCounter);
                    System.out.format("distance=%s\n", distance(lineI, data.get(k)).toPlainString());
                }
            }
//            System.out.format("counterMax=%d, t1Min=%s, t1Max=%s\n", counterMax, t1Min, t1Max);
//
//            if (counterMax > counterImax) {
//                counterImax = counterMax;
//            }
        }

//        System.out.format("counterImax=%d\n", counterImax);
    }

    public static Point3BigDecimal project(final Line3BigDecimal normalLine, final Point3BigDecimal point) {
        final var pointX = point.x();
        final var pointY = point.y();
        final var pointZ = point.z();

        final var p = normalLine.p();

        final var v = normalLine.v();
        final var dx = v.x();
        final var dy = v.y();
        final var dz = v.z();

        final var t = p.x().subtract(pointX).multiply(dx).add(p.y().subtract(pointY).multiply(dy)).add(p.z().subtract(pointZ).multiply(dz)).divide(dx.pow(2).add(dy.pow(2)).add(dz.pow(2)), CONTEXT);
        return new Point3BigDecimal(pointX.add(dx.multiply(t)), pointY.add(dy.multiply(t)), pointZ.add(dz.multiply(t)));
    }

    public static Line3BigDecimal project(final Line3BigDecimal normalLine, final Line3BigDecimal lineToProject) {
        return new Line3BigDecimal(project(normalLine, lineToProject.p()), project(normalLine, lineToProject.v()));
    }

//    private static Point3BigDecimal product(final Point3BigDecimal v1, final Point3BigDecimal v2) {
//    }

    private static BigDecimal distance(final Line3BigDecimal line1, final Line3BigDecimal line2) {
        final var x1 = line1.p().x();
        final var y1 = line1.p().y();
        final var z1 = line1.p().z();
        final var dx1 = line1.v().x();
        final var dy1 = line1.v().y();
        final var dz1 = line1.v().z();

        final var x2 = line2.p().x();
        final var y2 = line2.p().y();
        final var z2 = line2.p().z();
        final var dx2 = line2.v().x();
        final var dy2 = line2.v().y();
        final var dz2 = line2.v().z();

        final var dxProduct = dy1.multiply(dz2).subtract(dz1.multiply(dy2));
        final var dyProduct = dz1.multiply(dx2).subtract(dx1.multiply(dz2));
        final var dzProduct = dx1.multiply(dy2).subtract(dy1.multiply(dx2));

        final BigDecimal distance;
        if (dxProduct.equals(BigInteger.ZERO) && dyProduct.equals(BigInteger.ZERO) && dzProduct.equals(BigInteger.ZERO)) {
            final var xDiff = x2.subtract(x1);
            final var yDiff = y2.subtract(y1);
            final var zDiff = z2.subtract(z1);

            final var xProduct = dy1.multiply(zDiff).subtract(dz1.multiply(yDiff));
            final var yProduct = dz1.multiply(xDiff).subtract(dx1.multiply(zDiff));
            final var zProduct = dx1.multiply(yDiff).subtract(dy1.multiply(xDiff));

            distance = xProduct.pow(2).add(yProduct.pow(2)).add(zProduct.pow(2)).sqrt(CONTEXT).divide(dx1.pow(2).add(dy1.pow(2)).add(dz1.pow(2)).sqrt(CONTEXT), CONTEXT);
        } else
            distance = x2.subtract(x1).multiply(dxProduct).add(y2.subtract(y1).multiply(dyProduct)).add(z2.subtract(z1).multiply(dzProduct)).abs().divide(dxProduct.pow(2).add(dyProduct.pow(2)).add(dzProduct.pow(2)).sqrt(CONTEXT), CONTEXT);

//        System.out.format("\tline1=%s, line2=%s, distance=%s\n", line1, line2, distance.toPlainString());
        return distance;
    }
}

record Point3BigDecimal(BigDecimal x, BigDecimal y, BigDecimal z) {
    @Override
    public String toString() {
        return '(' + x.toPlainString() + ", " + y.toPlainString() + ", " + z.toPlainString() + ')';
    }
}

record Line3BigDecimal(Point3BigDecimal p, Point3BigDecimal v) {
    @Override
    public String toString() {
        return p + " -> " + v;
    }
}
