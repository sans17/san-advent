package us.ligusan.advent.advent2023.d12;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D12p1 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D12p1.class.getResourceAsStream("input.txt"))) {
            System.out.println(scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.format("s=%s\n", s);

                final var split = s.split(" ");
                final var list = Pattern.compile("\\d+").matcher(split[1]).results().map(m -> Integer.parseInt(m.group())).collect(Collectors.toList());

                var limit = BigInteger.ONE;
                for (int i = 0; i < Pattern.compile("\\?").matcher(split[0]).results().count(); i++) limit = limit.multiply(BigInteger.valueOf(2));

                var count = 0;
                for (BigInteger i = BigInteger.ZERO; i.compareTo(limit) < 0; i = i.add(BigInteger.ONE)) {
                    var replacement = limit.add(i).toString(2).substring(1).replace('0', '.').replace('1', '#');

                    var newString = new StringBuilder(split[0]);
                    var index = 0;

                    var matcher = Pattern.compile("\\?").matcher(split[0]);
                    while(matcher.find())
                        newString.setCharAt(matcher.start(), replacement.charAt(index++));

//                    System.out.format("\t%s\n", newString);

                    if(list.equals(Pattern.compile("#+").matcher(newString).results().map(m -> m.group().length()).collect(Collectors.toList()))) count++;
//                    System.out.format("\t\tcount=%d\n", count);
                }

                return count;
            }).reduce(Integer::sum));
        }
    }
}
