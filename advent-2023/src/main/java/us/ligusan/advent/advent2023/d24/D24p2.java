package us.ligusan.advent.advent2023.d24;

import us.ligusan.advent.advent2023.Point;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D24p2 {
    private static final MathContext CONTEXT = new MathContext(20);

    public static void main(final String[] args) {
        final var inputFileName = "input.txt";

//        final var inputFileName = "testInput.txt";

        List<Line3> data;

        try (var scanner = new Scanner(D24p1.class.getResourceAsStream(inputFileName))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.format("s=%s\n", s);
                final var matchResult = Pattern.compile("(\\d+), +(\\d+), +(\\d+) @ +(-?\\d+), +(-?\\d+), +(-?\\d+)").matcher(s).results().findFirst().get();
                return new Line3(new Point3Big(new BigInteger(matchResult.group(1)), new BigInteger(matchResult.group(2)), new BigInteger(matchResult.group(3))), new Point3Big(new BigInteger(matchResult.group(4)), new BigInteger(matchResult.group(5)), new BigInteger(matchResult.group(6))));
            }).collect(Collectors.toList());
        }
        System.out.println(data);

        final var size = data.size();

//        for (int i = 0; i < size; i++) {
//            final var v = data.get(i).v();
//            final var dx = v.x();
//            final var dy = v.y();
//            final var dz = v.z();
//
//            final var vLength = new BigDecimal(dx.pow(2).add(dy.pow(2)).add(dz.pow(2))).sqrt(mathContext);
//            System.out.format("i=%d, vLength=%s\n", i, vLength.toPlainString());
//        }

        final var distanceMap = new HashMap<Point, BigDecimal>();

        for (int i = 0; i < size - 1; i++)
            for (int j = i + 1; j < size; j++)
                distanceMap.put(new Point(i, j), distance(data.get(i), data.get(j)));

//        Stream.iterate(0, i -> i < size, i -> i + 1).forEach(i -> {
//            System.out.format("%s\n", distanceMap.entrySet().stream().filter(entry -> {
//                final var key = entry.getKey();
//                return key.x() == i || key.y() == i;
//            }).min(Map.Entry.comparingByValue()).get());
//        });

        final var minEntry = distanceMap.entrySet().stream().min(Map.Entry.comparingByValue()).get();
        final var minEntryKey = minEntry.getKey();

        final var i1 = minEntryKey.x();
        final var line1 = data.get(i1);

        final var i2 = minEntryKey.y();
        final var line2 = data.get(i2);

        System.out.format("minEntry=%s\n", minEntry);
        System.out.format("line(%d)=%s\n", i1, line1);
        System.out.format("line(%d)=%s\n", i2, line2);

        final var p1 = line1.p();
        final var v1 = line1.v();
        final var x1 = p1.x();
        final var y1 = p1.y();
        final var z1 = p1.z();
        final var dx1 = v1.x();
        final var dy1 = v1.y();
        final var dz1 = v1.z();

        final var p2 = line2.p();
        final var v2 = line2.v();
        final var x2 = p2.x();
        final var y2 = p2.y();
        final var z2 = p2.z();
        final var dx2 = v2.x();
        final var dy2 = v2.y();
        final var dz2 = v2.z();

        final var v1Length = new BigDecimal(dx1.pow(2).add(dy1.pow(2)).add(dz1.pow(2))).sqrt(CONTEXT);
        final var v2Length = new BigDecimal(dx2.pow(2).add(dy2.pow(2)).add(dz2.pow(2))).sqrt(CONTEXT);
        System.out.format("v1Length=%s, v2Length=%s\n", v1Length.toPlainString(), v2Length.toPlainString());

        final var maxD = v1Length.max(v2Length);
        System.out.format("maxD=%s\n", maxD.toPlainString());

        var nextP1 = new Point3Big(x1, y1, z1);
        var nextP2 = new Point3Big(x2, y2, z2);
        System.out.format("nextP1=%s, nextP2=%s\n", nextP1, nextP2);

        var distance = new BigDecimal(x2.subtract(x1).pow(2).add(y2.subtract(y1).pow(2)).add(z2.subtract(z1).pow(2))).sqrt(CONTEXT);

        var closestSteps = BigInteger.ZERO;

        var numberOfSteps = distance.divide(maxD, CONTEXT).toBigInteger();
        System.out.format("distance=%s\n", distance.toBigInteger());
        for (; ; ) {
            System.out.format("nextP1=%s, nextP2=%s\n", nextP1, nextP2);
            System.out.format("distance=%s\n", distance.toPlainString());

            System.out.format("numberOfSteps=%s\n", numberOfSteps);
            closestSteps = closestSteps.add(numberOfSteps);

            final var newNextP1 = new Point3Big(nextP1.x().add(dx1.multiply(numberOfSteps)), nextP1.y().add(dy1.multiply(numberOfSteps)), nextP1.z().add(dz1.multiply(numberOfSteps)));
            final var newNextP2 = new Point3Big(nextP2.x().add(dx2.multiply(numberOfSteps)), nextP2.y().add(dy2.multiply(numberOfSteps)), nextP2.z().add(dz2.multiply(numberOfSteps)));

            final var newDistance = new BigDecimal(newNextP2.x().subtract(newNextP1.x()).pow(2).add(newNextP2.y().subtract(newNextP1.y()).pow(2)).add(newNextP2.z().subtract(newNextP1.z()).pow(2))).sqrt(CONTEXT);
            System.out.println();
            if (newDistance.compareTo(distance) >= 0) {
                numberOfSteps = numberOfSteps.divide(BigInteger.valueOf(2));

                if (numberOfSteps.compareTo(BigInteger.ONE) < 0)
                    break;
            } else {
                nextP1 = newNextP1;
                nextP2 = newNextP2;

                distance = newDistance;
                numberOfSteps = distance.divide(maxD, CONTEXT).toBigInteger();
            }
        }
        System.out.format("closestSteps=%s\n", closestSteps);

        outerDiffLoop:
        for (int maxDiff = 0; ; maxDiff++) {
            System.out.format("maxDiff=%d\n", maxDiff);

            for (int iDiff = -maxDiff; iDiff <= maxDiff; iDiff++) {
                final var iSteps = closestSteps.add(BigInteger.valueOf(iDiff));
//                System.out.format("iSteps=%d ", iSteps);

                final var iPointX = p1.x().add(dx1.multiply(iSteps));
                final var iPointY = p1.y().add(dy1.multiply(iSteps));
                final var iPointZ = p1.z().add(dz1.multiply(iSteps));
                final var iPoint = new Point3Big(iPointX, iPointY, iPointZ);
//                System.out.format("iPoint=%s\n", iPoint);

                for (int jDiff = -maxDiff - 1; jDiff <= maxDiff; jDiff = Math.abs(iDiff) < maxDiff && jDiff < 0 ? maxDiff : jDiff, jDiff++) {
                    if (iDiff == jDiff)
                        continue;

                    final var jSteps = closestSteps.add(BigInteger.valueOf(jDiff));
//                    System.out.format("\tjSteps=%d ", jSteps);

                    final var stoneLine = new Line3(iPoint, new Point3Big(p2.x().add(dx2.multiply(jSteps)).subtract(iPointX), p2.y().add(dy2.multiply(jSteps)).subtract(iPointY), p2.z().add(dz2.multiply(jSteps)).subtract(iPointZ)));
//                    System.out.format("stoneLine=%s ", stoneLine);

                    boolean found = true;
                    for (int i = 0; i < size; i++) {
                        if (i == i1 || i == i2) continue;

                        final var iDistance = distance(data.get(i), stoneLine);
//                        System.out.format("i=%d, iDistance=%s\n", i, iDistance.toPlainString());

                        if (iDistance.compareTo(BigDecimal.ONE) >= 0) {
                            found = false;
                            break;
                        }
                    }

                    if (found) {
                        System.out.println("----");
                        System.out.format("stoneLine=%s\n", stoneLine);
                        System.out.format("iDiff=%d, jDiff=%d\n", iDiff, jDiff);

                        break outerDiffLoop;
                    }
                }
            }
        }


//        final var planes = Stream.iterate(0, i -> i < size - 1, i -> i + 1).<Plane>mapMulti((i, consumer) -> {
//        outer:
//        for (var turn = BigInteger.valueOf(2); ; turn = turn.add(BigInteger.ONE)) {
//            System.out.format("turn=%s\n", turn);
//
//            for (int i = 0; i < size; i++) {
//                final var line1 = data.get(i);
//
//                final var x1 = line1.p().x();
//                final var y1 = line1.p().y();
//                final var z1 = line1.p().z();
//                final var dx1 = line1.v().x();
//                final var dy1 = line1.v().y();
//                final var dz1 = line1.v().z();
//
//                final var x1next = x1.add(dx1.multiply(turn));
//                final var y1next = y1.add(dy1.multiply(turn));
//                final var z1next = z1.add(dz1.multiply(turn));
//
//                for (int j = 0; j < size; j++) {
//                    if (i == j) continue;
//
////                System.out.format("\ti=%d, j=%d:\n", i, j);
//
//                    final var line2 = data.get(j);
//
//                    final var x2 = line2.p().x();
//                    final var y2 = line2.p().y();
//                    final var z2 = line2.p().z();
//                    final var dx2 = line2.v().x();
//                    final var dy2 = line2.v().y();
//                    final var dz2 = line2.v().z();
//
//                    final var x2next = x2.add(dx2);
//                    final var y2next = y2.add(dy2);
//                    final var z2next = z2.add(dz2);
//
//                    final var turnMinusOne = turn.subtract(BigInteger.ONE);
//
//                    final var stoneDxDivideAndRemainder = x1next.subtract(x2next).divideAndRemainder(turnMinusOne);
//                    if (!stoneDxDivideAndRemainder[1].equals(BigInteger.ZERO)) continue;
//                    final var stoneDx = stoneDxDivideAndRemainder[0];
//
//                    final var stoneDyDivideAndRemainder = y1next.subtract(y2next).divideAndRemainder(turnMinusOne);
//                    if (!stoneDyDivideAndRemainder[1].equals(BigInteger.ZERO)) continue;
//                    final var stoneDy = stoneDyDivideAndRemainder[0];
//
//                    final var stoneDzDivideAndRemainder = z1next.subtract(z2next).divideAndRemainder(turnMinusOne);
//                    if (!stoneDzDivideAndRemainder[1].equals(BigInteger.ZERO)) continue;
//                    final var stoneDz = stoneDzDivideAndRemainder[0];
//
//                    final var stoneStartX = x2next.subtract(stoneDx);
//                    final var stoneStartY = y2next.subtract(stoneDy);
//                    final var stoneStartZ = z2next.subtract(stoneDz);
//
//                    final var stoneLine = new Line3(new Point3Big(stoneStartX, stoneStartY, stoneStartZ), new Point3Big(stoneDx, stoneDy, stoneDz));
//
////                        System.out.format("stoneLine=%s\n", stoneLine);
//
//                    int counter = 0;
//                    for (int k = 0; k < size; k++) {
//                        if (k == i || k == j) continue;
////                            System.out.format("\tk=%d\n", k);
//
//                        final var lineToTest = data.get(k);
//
//                        final var xToTest = lineToTest.p().x();
//                        final var yToTest = lineToTest.p().y();
//                        final var zToTest = lineToTest.p().z();
//
//                        final var dxToTest = lineToTest.v().x();
//                        final var dyToTest = lineToTest.v().y();
//                        final var dzToTest = lineToTest.v().z();
//
//                        final var divisor = dxToTest.multiply(stoneDy).subtract(stoneDx.multiply(dyToTest));
////                            System.out.format("divisor=%s ", divisor);
//                        if (divisor.equals(BigInteger.ZERO)) {
//                            continue;
//                        }
//
//                        final var divideAndRemainder = stoneStartX.subtract(xToTest).multiply(dyToTest).multiply(stoneDy).subtract(stoneStartY.multiply(stoneDx).multiply(dyToTest)).add(yToTest.multiply(dxToTest).multiply(stoneDy)).divideAndRemainder(divisor);
//
//                        if (!divideAndRemainder[1].equals(BigInteger.ZERO))
//                            continue;
//
//                        final var y = divideAndRemainder[0];
//
//                        final var t1DivideAndRemainder = y.subtract(yToTest).divideAndRemainder(dyToTest);
//                        final var t1 = t1DivideAndRemainder[0];
//
////                        System.out.format("y=%s, t1=%s\n", y, t1);
//                        if (t1.compareTo(BigInteger.ZERO) < 0 || !t1DivideAndRemainder[1].equals(BigInteger.ZERO)) {
//                            continue;
//                        }
//
//                        final var x = xToTest.add(dxToTest.multiply(t1));
//                        final var z = zToTest.add(dzToTest.multiply(t1));
//                        final var stoneX = stoneStartX.add(stoneDx.multiply(t1));
//                        final var stoneZ = stoneStartZ.add(stoneDz.multiply(t1));
////System.out.format("x=%s, z=%s, stoneX=%s, stoneZ=%s\n", x, z, stoneX, stoneZ);
//
//                        if (!x.equals(stoneX) || !z.equals(stoneZ))
//                            continue;
//
//                        counter++;
//                    }
//
//                    if (counter == size - 2) {
//
//                        System.out.println("----");
//                        System.out.format("stoneLine=%s", stoneLine);
//
//                        break outer;
//                    }
//
////                    if (turn.equals(BigInteger.valueOf(3)) && i == 1 && j == 4) break outer;
//                }
//            }
//    }
    }

    private static BigDecimal distance(final Line3 line1, final Line3 line2) {
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

//                    final var x2next = x2.add(dx2.multiply(turn));
//                    final var y2next = y2.add(dy2.multiply(turn));
//                    final var z2next = z2.add(dz2.multiply(turn));
//
//                    final var distnace = x2next.subtract(x1next).pow(2).add(y2next.subtract(y1next).pow(2)).add(z2next.subtract(z1next).pow(2)).sqrt();

//                    minDistnace.compute(new Point(i, j), (key, value) -> value == null ? distnace : distnace.min(value));

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

            distance = new BigDecimal(xProduct.pow(2).add(yProduct.pow(2)).add(zProduct.pow(2))).sqrt(CONTEXT).divide(new BigDecimal(dx1.pow(2).add(dy1.pow(2)).add(dz1.pow(2))).sqrt(CONTEXT), CONTEXT);
        } else
            distance = new BigDecimal(x2.subtract(x1).multiply(dxProduct).add(y2.subtract(y1).multiply(dyProduct)).add(z2.subtract(z1).multiply(dzProduct))).abs().divide(new BigDecimal(dxProduct.pow(2).add(dyProduct.pow(2)).add(dzProduct.pow(2))).sqrt(CONTEXT), CONTEXT);

//        System.out.format("\tline1=%s, line2=%s, distance=%s\n", line1, line2, distance.toPlainString());
        return distance;
    }
}

