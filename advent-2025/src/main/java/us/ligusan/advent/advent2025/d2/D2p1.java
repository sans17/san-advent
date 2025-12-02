package us.ligusan.advent.advent2025.d2;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D2p1
{
    static void main()
    {
        AtomicLong result = new AtomicLong();

        try(var scanner = new Scanner(D2p1.class.getResourceAsStream("input.txt")))
        {
            scanner.useDelimiter(",").tokens().forEach(s -> {
                final String input = s.trim();
                System.out.println(input);

                Matcher m = Pattern.compile("(\\d+)-(\\d+)").matcher(input);
                m.find();

                final String g1 = m.group(1);
                final long bottomValue = Long.parseLong(g1);
                final long topValue = Long.parseLong(m.group(2));
                final String g1p1 = g1.substring(0, g1.length() / 2);
                for(int i = g1p1.isEmpty() ? 1 : Integer.parseInt(g1p1); ; i++)
                {
                    final String iString = Integer.toString(i);
                    final long testValue = Long.parseLong(iString + iString);
                    if(testValue > topValue) break;
                    if(bottomValue <= testValue && testValue <= topValue)
                    {
                        System.out.printf("\t%d%n", testValue);

                        result.addAndGet(testValue);
                    }
                }
            });
        }

        System.out.printf("result=%s", result);
    }
}
