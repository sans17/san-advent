package us.ligusan.advent.advent2024.d3;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class D3p2 {
    public static void main(final String[] args) throws Exception {
        var result = new AtomicInteger();
        final var doFlag = new AtomicBoolean(true);
        try(var scanner = new Scanner(D3p2.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                Pattern.compile("mul\\((\\d+),(\\d+)\\)|do(n't)?\\(\\)").matcher(s).results().forEach(m -> {
                    switch(m.group(0).substring(0, 3)) {
                        case "mul":
                            if(doFlag.get())
                                result.addAndGet(Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2)));
                            break;
                        case "do(":
                            doFlag.set(true);
                            break;
                        case "don":
                            doFlag.set(false);
                            break;
                        default:
                    }
                });
            });
        }
        System.out.println(result);
    }
}
