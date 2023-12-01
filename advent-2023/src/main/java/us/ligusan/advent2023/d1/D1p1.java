package us.ligusan.advent2023.d1;

import java.util.Scanner;

public class D1p1 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D1p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().mapToInt(s -> {
                int ret = 0;
                final var chars = s.toCharArray();

                int i = 0;
                for (; i < chars.length; i++) {
                    if (Character.isDigit(chars[i])) {
                        ret = (chars[i] - '0') * 10;
                        break;
                    }
                }

                int j = chars.length - 1;
                for (; j >= 0; j--) {
                    if (Character.isDigit(chars[j])) {
                        ret += (chars[j] - '0');
                        break;
                    }
                }

//                System.out.format("s=%s, ret=%d\n", s, ret, chars[i], chars[j]);

                return ret;
            }).reduce(Integer::sum).ifPresent(System.out::println);
        }
    }
}
