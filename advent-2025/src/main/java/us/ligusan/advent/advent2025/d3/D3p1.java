package us.ligusan.advent.advent2025.d3;

import java.util.List;
import java.util.Scanner;

public class D3p1
{
    static void main()
    {
        final List<String> input;
        try(var scanner = new Scanner(D3p1.class.getResourceAsStream("input.txt")))
        {
            input = scanner.useDelimiter("\r?\n").tokens().toList();
        }

        System.out.println(input.stream().mapToInt(s -> {
            int ret = 0;

            final int len = s.length();
            for(int i = 0; i < len - 1; i++) for(int j = i + 1; j < len; j++) ret = Math.max(ret, Integer.parseInt("" + s.charAt(i) + s.charAt(j)));

            System.out.printf("%s : %d%n", s, ret);

            return ret;
        }).reduce(Integer::sum).orElse(0));
    }
}
