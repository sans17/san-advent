package us.ligusan.advent.advent2025.d1;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D1p1
{
    static final int MOD = 100;

    static void main()
    {
        AtomicInteger pointer = new AtomicInteger(50);
        AtomicInteger counter = new AtomicInteger();

        try(var scanner = new Scanner(D1p1.class.getResourceAsStream("input.txt")))
        {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
//                System.out.println(s);

                Matcher m = Pattern.compile("([LR])(\\d+)").matcher(s);
                m.find();

                int pStart = pointer.get();
                int p = pStart + ("L".equals(m.group(1)) ? -1 : 1) * (Integer.parseInt(m.group(2)) % MOD);
                if(p < 0) p += MOD;
                if(p >= MOD) p -= MOD;
                if(p == 0) counter.incrementAndGet();

                pointer.set(p);

                System.out.printf("%d %s : %d%n", pStart, s, p);
            });
        }

        System.out.println(counter);
    }
}
