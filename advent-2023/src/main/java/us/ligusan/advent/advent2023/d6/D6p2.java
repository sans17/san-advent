package us.ligusan.advent.advent2023.d6;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D6p2 {
    public static void main(final String[] args) {
        final List<BigInteger> data;

        try (var scanner = new Scanner(D6p2.class.getResourceAsStream("input.txt"))) {
            data = scanner.useDelimiter("\r?\n").tokens().map(s -> new BigInteger(s.split(":")[1].replaceAll(" ", ""))).collect(Collectors.toList());
        }
        System.out.format("data=%s\n", data);

        final var time = data.get(0);
        final var distance = data.get(1);

        var counter = BigInteger.ZERO;
        for (BigInteger i = BigInteger.ZERO; ; i = i.add(BigInteger.ONE))
            if (i.multiply(time.subtract(i)).compareTo(distance) <= 0) counter = counter.add(BigInteger.ONE);
            else break;
        for (BigInteger i = time; ; i = i.subtract(BigInteger.ONE))
            if (i.multiply(time.subtract(i)).compareTo(distance) <= 0) counter = counter.add(BigInteger.ONE);
            else break;

        System.out.println(time.add(BigInteger.ONE).subtract(counter));
    }
}
