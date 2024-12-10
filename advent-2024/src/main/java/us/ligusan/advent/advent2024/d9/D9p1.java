package us.ligusan.advent.advent2024.d9;

import java.util.Scanner;

public class D9p1 {
    public static void main(final String[] args) throws Exception {
        String s;
        try(var scanner = new Scanner(D9p1.class.getResourceAsStream("input.txt"))) {
            s = scanner.nextLine();
        }
        System.out.println(s);

        var sLength = s.length();

        long result = 0;
        var counter = 0;

        var k = sLength - 1 - (sLength % 2 == 0 ? 1 : 0);
        var j0 = 0;
        var j1 = s.charAt(k) - '0';
        for(int i = 0; i <= k; ) {
            System.out.format("i=%d, k=%d, j0=%d, j1=%d%n", i, k, j0, j1);
            if(i % 2 == 0) {
                for(j0 = 0; j0 < (i < k ? s.charAt(i) - '0' : j1); j0++, counter++) {
                    result += counter * (i / 2);
                    System.out.format("0: j0=%d, j1=%d, counter=%d, result=%d%n", j0, j1, counter, result);
                }
                i++;
                j0 = 0;
                continue;
            }

            for(; ; j0++, j1--, counter++) {
                if(j0 >= s.charAt(i) - '0') {
                    i++;
                    j0 = 0;
                    break;
                }
                if(j1 <= 0) {
                    k -= 2;
                    j1 = s.charAt(k) - '0';
                    break;
                }
                result += counter * (k / 2);
                System.out.format("1: j0=%d, j1=%d, counter=%d, result=%d%n", j0, j1, counter, result);
            }
        }
        System.out.println(result);
    }
}
