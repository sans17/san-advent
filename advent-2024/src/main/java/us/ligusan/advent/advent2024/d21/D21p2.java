package us.ligusan.advent.advent2024.d21;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D21p2 {
    private static final int LEVELS = 25;

    private static final Map<Integer, Map<Map.Entry<Character, Character>, Map.Entry<String, Long>>> SHORTEST = new HashMap<>();

    private static final List<List<Character>> PAD1 = List.of(List.of('7', '8', '9'), List.of('4', '5', '6'), List.of('1', '2', '3'), Arrays.asList(null, '0', 'A'));
    private static final List<List<Character>> PAD2 = List.of(Arrays.asList(null, '^', 'A'), List.of('<', 'v', '>'));

    public static void main(final String[] args) throws Exception {
        var result = 0L;

        try (var scanner = new Scanner(D21p2.class.getResourceAsStream("input.txt"))) {
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                System.out.println(s);

                var l = find(0, s);
                var v = Integer.parseInt(s.substring(0, s.length() - 1));
                var complexity = l * v;
                System.out.format("l=%d v=%d, complexity=%d%n", l, v, complexity);
                result += complexity;
            }
        }
        System.out.println(result);

        for (var entry : SHORTEST.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    private static Long find(final int level, final String sequence) {
//        System.out.format("level=%d, sequence=%s%n", level, sequence);
        var s1 = 'A' + sequence;
        var ret = 0L;
        for (int i = 0; i < s1.length() - 1; i++) ret += find(level, s1.charAt(i), s1.charAt(i + 1)).getValue();
        System.out.format("level=%d, sequence=%s, ret=%s%n", level, sequence, ret);
        return ret;
    }

    private static Set<String> getPaths(final List<List<Character>> pad, final int xFrom, final int yFrom, final int xTo, final int yTo) {
        var xs = xTo > xFrom ? ">" : "<";
        if (yTo == yFrom) return Set.of(xs.repeat(Math.abs(xTo - xFrom)));
        var ys = yTo > yFrom ? "v" : "^";
        if (xTo == xFrom) return Set.of(ys.repeat(Math.abs(yTo - yFrom)));

        var nextX = xFrom + (int) Math.signum(xTo - xFrom);
        var nextY = yFrom + (int) Math.signum(yTo - yFrom);

        var streamX = getPaths(pad, nextX, yFrom, xTo, yTo).stream().map(s -> xs + s);
        var streamY = getPaths(pad, xFrom, nextY, xTo, yTo).stream().map(s -> ys + s);
        return (pad.get(yFrom).get(nextX) == null ? streamY : pad.get(nextY).get(xFrom) == null ? streamX : Stream.concat(streamX, streamY)).collect(Collectors.toSet());
    }

    private static Map.Entry<String, Long> find(final int level, final char from, final char to) {
        return SHORTEST.computeIfAbsent(level, _ -> new HashMap<>()).computeIfAbsent(Map.entry(from, to), _ -> {
            var pad = level == 0 ? PAD1 : PAD2;

            var xFrom = -1;
            var yFrom = -1;
            var xTo = -1;
            var yTo = -1;

            var found = 0;
            outer:
            for (int i = 0; i < pad.size(); i++) {
                var l = pad.get(i);
                for (int j = 0; j < l.size(); j++) {
                    Character c = l.get(j);
                    if (c != null) {
                        if (c == from) {
                            xFrom = j;
                            yFrom = i;
                            found++;
                        }
                        if (c == to) {
                            xTo = j;
                            yTo = i;
                            found++;
                        }
                        if (found == 2) break outer;
                    }
                }
            }

            if (level == LEVELS || xFrom == xTo || yFrom == yTo) {
                var sb = new StringBuilder();
                for (var j = yFrom; j < yTo; j++) sb.append('v');
                for (var j = xFrom; j > xTo; j--) sb.append('<');
                for (var j = xFrom; j < xTo; j++) sb.append('>');
                for (var j = yFrom; j > yTo; j--) sb.append('^');

                var s = sb.append('A').toString();
                return level == LEVELS ? Map.entry(s, (long) s.length()) : Map.entry(s, find(level + 1, s));
            } else {
                Map.Entry<String, Long> ret = null;
                for (var s : getPaths(pad, xFrom, yFrom, xTo, yTo)) {
                    s += 'A';
                    var test = find(level + 1, s);
                    if (ret == null || test < ret.getValue()) ret = Map.entry(s, test);
                }
                return ret;
            }
        });
    }
}