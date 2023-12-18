package us.ligusan.advent.advent2023.d18;

import us.ligusan.advent.advent2023.Point;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D18p2 {
    public static void main(final String[] args) {
        final List<Map.Entry<Character, Integer>> data;

        try (var scanner = new Scanner(D18p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> {
                final var matchResult = Pattern.compile("\\(#([a-f0-9]{5})([0-3])\\)").matcher(s).results().findFirst().get();
                return Map.entry(matchResult.group(2).charAt(0), Integer.parseInt(matchResult.group(1), 16));
            }).collect(Collectors.toList());
        }

        data.forEach(entry -> System.out.format("%s\n", entry));

        var currentPosition = new Point(0, 0);
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        for (final var dataEntry : data) {
            final var direction = dataEntry.getKey();
            final var distance = dataEntry.getValue();

            final int newX = currentPosition.x() + switch (direction) {
                case '0' -> distance;
                case '2' -> -distance;
                default -> 0;
            };
            final int newY = currentPosition.y() + switch (direction) {
                case '3' -> -distance;
                case '1' -> distance;
                default -> 0;
            };

            currentPosition = new Point(newX, newY);
            if (newX < minX) minX = newX;
            if (newX > maxX) maxX = newX;
            if (newY < minY) minY = newY;
            if (newY > maxY) maxY = newY;
        }
        System.out.format("minX=%d, maxX=%d, minY=%d, maxY=%d\n", minX, maxX, minY, maxY);

        final var dataSize = data.size();

        var area = BigInteger.ZERO;
        currentPosition = new Point(0, 0);
        for(int i = 0 ; i < data.size(); i++) {
            final var dataEntry = data.get(i);
            final var direction = dataEntry.getKey();
            final var distance = dataEntry.getValue();

            final var prevEntry = data.get((i + dataSize - 1) % dataSize);
            final var prevDirection = prevEntry.getKey();

            final var nextEntry = data.get((i + 1) % dataSize);
            final var nextDirection = nextEntry.getKey();

            switch (direction) {
                case '0':
                    var x = BigInteger.valueOf(distance);
                    if (prevDirection == '3')
                        x = x.add(BigInteger.ONE);
                    if (nextDirection == '3')
                        x = x.subtract(BigInteger.ONE);
                    var y = BigInteger.valueOf(maxY - currentPosition.y() + 1);
                    area = area.add(x.multiply(y));

                    currentPosition = new Point(currentPosition.x() + distance, currentPosition.y());
                    break;

                case '2':
                    x = BigInteger.valueOf(distance);
                    if (prevDirection == '1')
                        x = x.add(BigInteger.ONE);
                    if (nextDirection == '1')
                        x = x.subtract(BigInteger.ONE);
                    y = BigInteger.valueOf(maxY - currentPosition.y());
                    area = area.subtract(x.multiply(y));

                    currentPosition = new Point(currentPosition.x() - distance, currentPosition.y());
                    break;

                case '1':
                    x = BigInteger.valueOf(currentPosition.x() - minX + 1);
                    y = BigInteger.valueOf(distance);
                    if (prevDirection == '0')
                        y = y.add(BigInteger.ONE);
                    if (nextDirection == '0')
                        y = y.subtract(BigInteger.ONE);
                    area = area.add(x.multiply(y));

                    currentPosition = new Point(currentPosition.x(), currentPosition.y() + distance);
                    break;

                case '3':
                    x = BigInteger.valueOf(currentPosition.x() - minX);
                    y = BigInteger.valueOf(distance);
                    if (prevDirection == '2')
                        y = y.add(BigInteger.ONE);
                    if (nextDirection == '2')
                        y = y.subtract(BigInteger.ONE);
                    area = area.subtract(x.multiply(y));

                    currentPosition = new Point(currentPosition.x(), currentPosition.y() - distance);
                    break;
            }
        }
        System.out.format("area=%s\n", area.divide(BigInteger.valueOf(2)));
    }
}
