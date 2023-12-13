package us.ligusan.advent.advent2023.d12;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D12p2 {
    public static void main(final String[] args) {
        try (var scanner = new Scanner(D12p2.class.getResourceAsStream("input.txt"))) {
            System.out.println(scanner.useDelimiter("\r?\n").tokens().map(s -> {
                System.out.format("s=%s\n", s);

                final var split = s.split(" ");
                final var list = Pattern.compile("\\d+").matcher(split[1]).results().map(m -> Integer.parseInt(m.group())).collect(Collectors.toList());

                var unfolded = Collections.nCopies(5, split[0]).stream().collect(Collectors.joining("?"));
                var unfoldedList = Collections.nCopies(5, list).stream().flatMap(l -> l.stream()).collect(Collectors.toList());
                System.out.format("unfolded=%s\n", unfolded);
                System.out.format("unfoldedList=%s\n", unfoldedList);

                return count(unfolded, unfoldedList);
            }).reduce((a, b) -> a.add(b)));
        }
    }

    private final static Map<Map.Entry<String, List<Integer>>, BigInteger> COUNT_CACHE = new HashMap<>();

    private static BigInteger count(final String s, final List<Integer> list) {
        var ret = COUNT_CACHE.get(Map.entry(s, list));
        if (ret != null) return ret;

//        System.out.format("count: s=%s, list=%s\n", s, list);

        if (list.isEmpty()) return s.indexOf('#') >= 0 ? BigInteger.ZERO : BigInteger.ONE;
        final var sNoDots = s.replace(".", "");
        final var listSum = list.stream().reduce(Integer::sum).orElse(0);
        if(sNoDots.length() < listSum) return BigInteger.ZERO;
        if(sNoDots.replace("?", "").length() > listSum) return BigInteger.ZERO;

        final var max = list.stream().reduce(Integer::max).get();
        var maxIndex = list.indexOf(max);
//        System.out.format("count: max=%d, maxIndex=%d\n", max, maxIndex);

        final var matcher = Pattern.compile("[\\?#]{" + max + '}').matcher(s);

        ret = BigInteger.ZERO;
        for (int i = 0 ; matcher.find(i) ; i++) {
            final var matcherStart = matcher.start();
            final var notFirstFlag = matcherStart > 0;
            if(notFirstFlag && s.charAt(matcherStart - 1) == '#') continue;

            final var matcherEnd = matcher.end();
            final var notLastFlag = matcherEnd < s.length();
            if(notLastFlag && s.charAt(matcherEnd) == '#') continue;

            final var s1 = notFirstFlag ? s.substring(0, matcherStart - 1) : "";
            final var s2 = notLastFlag ? s.substring(matcherEnd + 1) : "";
//            System.out.format("\tcount: i=%d, matcherStart=%d, matcherEnd=%d, s1=%s, s2=%s\n", i, matcherStart, matcherEnd, s1, s2);

            i = matcherStart;

            var ret1 = BigInteger.ZERO;
            var ret2 = BigInteger.ZERO;
            final var firstFlag = s1.length() <= s2.length();
            if (firstFlag && BigInteger.ZERO.equals(ret1 = count(s1, list.subList(0, maxIndex)))) continue;
            if (BigInteger.ZERO.equals(ret2 = count(s2, list.subList(maxIndex + 1, list.size())))) continue;
            if (!firstFlag && BigInteger.ZERO.equals(ret1 = count(s1, list.subList(0, maxIndex)))) continue;

//            System.out.format("\tcount: ret1=%d, ret2=%d\n", ret1, ret2);

            ret = ret.add(ret1.multiply(ret2));
        }

        System.out.format("count: s=%s, list=%s, ret=%d\n", s, list, ret);

        COUNT_CACHE.put(Map.entry(s, list), ret);
        return ret;
    }

}
