package us.ligusan.advent.advent2025.d11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class D11p2
{
    static void main()
    {
        var filename = "input.txt";

        final Map<String, List<String>> input;
        try(var scanner = new Scanner(D11p2.class.getResourceAsStream(filename)))
        {
            input = scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).map(s -> {
                final String[] split = s.split(":");
                return Map.entry(split[0].trim(), Arrays.asList(split[1].trim().split("\\s+")));
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        System.out.println(input);

        var counter = compute("svr", "dac", List.of("svr", "fft", "out"), input, new HashMap<>()) * compute("dac", "fft", List.of("svr", "out"), input, new HashMap<>()) * compute("fft", "out", List.of("svr", "dac"), input, new HashMap<>());
        System.out.println(counter);
        counter += compute("svr", "fft", List.of("svr", "dac", "out"), input, new HashMap<>()) * compute("fft", "dac", List.of("svr", "out"), input, new HashMap<>()) * compute("dac", "out", List.of("svr", "fft"), input, new HashMap<>());
        System.out.println(counter);
    }

    static long compute(final String start, final String end, final List<String> exclude, final Map<String, List<String>> input, final Map<String, Long> cache)
    {
        if (end.equals(start)) return 1;

        var ret = cache.get(start);
        if (ret != null) return ret;

//        System.out.printf("%s %s %s%n", start, end, exclude);
        ret = input.get(start).stream().filter(s-> !exclude.contains(s)).mapToLong(s -> compute(s, end, exclude, input, cache)).sum();
        cache.put(start, ret);
        System.out.printf("%s %s %s: %d%n", start, end, exclude, ret);
        return ret;
    }
}
