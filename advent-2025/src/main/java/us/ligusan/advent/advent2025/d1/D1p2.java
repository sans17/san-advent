package us.ligusan.advent.advent2025.d1;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D1p2
{
    static final int MOD = 100;

    static void main()
    {
        AtomicInteger pointer = new AtomicInteger(50);
        AtomicInteger counter = new AtomicInteger();

        try(var scanner = new Scanner(D1p2.class.getResourceAsStream("input.txt")))
        {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
//                System.out.println(s);

                Matcher m = Pattern.compile("([LR])(\\d+)").matcher(s);
                m.find();

                int g2 = Integer.parseInt(m.group(2));
                int counterChange = g2 / MOD;
                g2 = g2 % MOD;

                int pStart = pointer.get();
                int p = pStart + ("L".equals(m.group(1)) ? -1 : 1) * g2;
                if(p == 0) counterChange++;
                if(p >= MOD)
                {
                    counterChange++;
                    p -= MOD;
                }
                if(p < 0)
                {
                    if(pStart > 0) counterChange++;
                    p += MOD;
                }

                pointer.set(p);
                counter.addAndGet(counterChange);

                System.out.printf("%d %s : %d : %d %d%n", pStart, s, p, counterChange, counter.get());
            });
        }

        System.out.println(counter);
    }
}
