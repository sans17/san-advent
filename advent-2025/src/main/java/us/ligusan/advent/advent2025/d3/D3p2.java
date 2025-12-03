package us.ligusan.advent.advent2025.d3;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class D3p2
{
    static void main()
    {
        final List<String> input;
        try(var scanner = new Scanner(D3p2.class.getResourceAsStream("input.txt")))
        {
            input = scanner.useDelimiter("\r?\n").tokens().toList();
        }

        System.out.println(input.stream().mapToLong(s -> {
            final long ret = max(s, 12);
            System.out.printf("%s : %d%n", s, ret);
            return ret;
        }).reduce(Long::sum).orElse(0));
    }

    static ConcurrentMap<String, ConcurrentMap<Integer, Long>> MAXES = new ConcurrentHashMap<>();
    static long max(final String s, final int n)
    {
        if(n == 0) return 0;
        if(s.length() == n) return Long.parseLong(s);
        if(n == 1) return s.chars().reduce(Integer::max).orElse('0') - '0';

        final long ret = MAXES.computeIfAbsent(s, _ -> new ConcurrentHashMap<>()).computeIfAbsent(n, _ -> {
            final String s1 = s.substring(1);
            return Math.max(Long.parseLong("" + s.charAt(0) + max(s1, n - 1)), max(s1, n));
        });
//        System.out.printf("\ts=%s, n=%d, ret=%d%n", s, n, ret);
        return ret;
    }
}
