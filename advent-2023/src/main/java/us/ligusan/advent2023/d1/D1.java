package us.ligusan.advent2023.d1;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class D1 {
    public static void main(final String[] args) {
        List<String> list;
        try (var scanner = new Scanner(D1.class.getResourceAsStream("input.txt"))) {
            list = scanner.useDelimiter("\r?\n").tokens().collect(Collectors.toList());
        }

        System.out.printf("list=%s%n", list);
    }
}
