package us.ligusan.advent.advent2024.d3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import us.ligusan.advent.advent2024.d2.D2p1;

public class D3p1 {
    public static void main(final String[] args) throws Exception {
        var result = new AtomicInteger();
        try (var scanner = new Scanner(D3p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                Pattern.compile("mul\\((\\d+),(\\d+)\\)").matcher(s).results()
                 .forEach(m -> result.addAndGet(Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2))));
            });
        }
        System.out.println(result);
    }
}
