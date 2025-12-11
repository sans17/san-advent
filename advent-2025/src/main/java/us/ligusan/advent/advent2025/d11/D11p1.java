package us.ligusan.advent.advent2025.d11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class D11p1
{
    static void main()
    {
        var filename = "input.txt";

        final Map<String, List<String>> input;
        try(var scanner = new Scanner(D11p1.class.getResourceAsStream(filename)))
        {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).map(s -> {
                final String[] split = s.split(":");
                return Map.entry(split[0].trim(), Arrays.asList(split[1].trim().split("\\s+")));
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        System.out.println(input);

        System.out.println(compute("you", input, new HashMap<>()));
    }

    static int compute(final String key, final Map<String, List<String>> input, final Map<String, Integer> cache)
    {
        if ("out".equals(key)) return 1;

        var ret = cache.get(key);
        if (ret != null) return ret;

        ret = input.get(key).stream().mapToInt(s -> compute(s, input, cache)).sum();
        cache.put(key, ret);
        System.out.printf("%s: %d%n", key, ret);
        return ret;
    }
}
