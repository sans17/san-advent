package us.ligusan.advent.advent2024.d13;

import java.util.Scanner;
import java.util.regex.Pattern;

public class D13p2 {
    public static void main(final String[] args) throws Exception {
        var result = 0L;

        try (var scanner = new Scanner(D13p2.class.getResourceAsStream("input.txt"))) {
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                System.out.println(s);
                var m = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)").matcher(s);
                m.find();
                var x1 = Integer.parseInt(m.group(1));
                var y1 = Integer.parseInt(m.group(2));
                System.out.format("x1=%d, y1=%d%n", x1, y1);

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)").matcher(s);
                m.find();
                var x2 = Integer.parseInt(m.group(1));
                var y2 = Integer.parseInt(m.group(2));
                System.out.format("x2=%d, y2=%d%n", x2, y2);

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)").matcher(s);
                m.find();
                var x0 = 10000000000000L + Integer.parseInt(m.group(1));
                var y0 = 10000000000000L + Integer.parseInt(m.group(2));
                System.out.format("x0=%d, y0=%d%n", x0, y0);

                var div = y1 * x2 - x1 * y2;
                if (div != 0) {
                    var n = (y0 * x2 - x0 * y2) / div;
                    var k = (x0 - x1 * n) / x2;
                    System.out.format("n=%d, k=%d%n", n, k);

                    if (n >= 0 && k >= 0 && x1 * n + x2 * k == x0 && y1 * n + y2 * k == y0) {
                        result += 3 * n + k;
                        System.out.format("result=%d%n", result);
                    }
                } else {
                    Long cost1 = null;
                    Long cost2 = null;
                    var n = x0 / x1;
                    if (x1 * n == x0 && y1 * n == y0) {
                        cost1 = 3 * n;
                        System.out.format("cost1=%d%n", cost1);
                    }
                    var k = x0 / x2;
                    if (x2 * k == x0 && y2 * k == y0) {
                        cost2 = k;
                        System.out.format("cost2=%d%n", cost2);
                    }
                    if (cost1 != null || cost2 != null) {
                        result += cost1 == null ? cost2 : cost2 == null ? cost1 : Math.min(cost1, cost2);
                        System.out.format("result=%d%n", result);
                    }
                }

                if (scanner.hasNextLine()) scanner.nextLine();

                System.out.println();
            }
        }
        System.out.println(result);
    }
}
