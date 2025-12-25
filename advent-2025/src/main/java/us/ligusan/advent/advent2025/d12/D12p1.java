package us.ligusan.advent.advent2025.d12;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D12p1 {
    static void main() {
//        var filename = "testInput.txt";
        var filename = "input.txt";

        var figs = new ArrayList<Set<List<Integer>>>();
        var regions = new ArrayList<Map.Entry<List<Integer>, List<Integer>>>();

        var figFlagRef = new AtomicBoolean(false);
        var xRef = new AtomicInteger();
        var yRef = new AtomicInteger();
        try (var scanner = new Scanner(D12p1.class.getResourceAsStream(filename))) {
            scanner.useDelimiter("\r?\n").tokens().peek(System.out::println).forEach(s -> {
                if (figFlagRef.get()) if (s.isBlank()) figFlagRef.set(false);
                else {
                    var y = yRef.getAndIncrement();
                    xRef.set(0);
                    figs.getLast().addAll(s.chars().boxed().map(c -> {
                        var x = xRef.getAndIncrement();
                        return c == '#' ? List.of(x, y) : null;
                    }).filter(Objects::nonNull).toList());
                }
                else if (s.indexOf('x') > 0) {
                    Matcher m = Pattern.compile("(\\d+)x(\\d+): (.*)").matcher(s);
                    m.find();

                    regions.add(Map.entry(List.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))), Arrays.stream(m.group(3).split(" ")).map(Integer::parseInt).toList()));
                } else {
                    figFlagRef.set(true);
                    figs.add(new HashSet<>());
                    yRef.set(0);
                }
            });
        }
        System.out.printf("figs=%s%n", figs);
        System.out.printf("regions=%s%n", regions);

        var allFigs = figs.stream().map(f -> {
            var ret = new HashSet<Set<List<Integer>>>();
            for (var j = 0; j < 2; j++) {
                ret.add(f);
                for (var i = 0; i < 3; i++) {
                    var maxy = f.stream().mapToInt(List::getLast).max().getAsInt();
                    ret.add(f = f.stream().map(xy -> List.of(maxy - xy.getLast(), xy.getFirst())).collect(Collectors.toSet()));
                }
                if (j == 0) f = f.stream().map(xy -> List.of(xy.getLast(), xy.getFirst())).collect(Collectors.toSet());
            }
            return ret.stream().toList();
        }).toList();
        var allFigsDimensions = Stream.iterate(0, i -> i < allFigs.size(), i -> i + 1).flatMap(i -> {
            var fi = allFigs.get(i);
            return Stream.iterate(0, j -> j < fi.size(), j -> j + 1).map(j -> {
                var fig = fi.get(j);
                return Map.entry(Map.entry(i, j), List.of(fig.stream().mapToInt(List::getFirst).max().orElse(0), fig.stream().mapToInt(List::getLast).max().orElse(0)));
            });
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.printf("allFigs=%s, allFigsDimensions=%s%n", allFigs, allFigsDimensions);

        var overlapsMap = new HashMap<Map.Entry<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>, List<Integer>>, Boolean>();

        System.out.println(Stream.iterate(0, i -> i < regions.size(), i -> i + 1).map(i -> //Map.entry()).sorted(Comparator.comparingInt(i -> 
        {
            var reg = regions.get(i);
            var k = reg.getKey();
            var l = reg.getValue();
            return Map.entry( reg,-k.getFirst() * k.getLast() + Stream.iterate(0, j -> j < l.size(), j -> j + 1).mapToInt(j -> l.get(j) * figs.get(j).size()).sum());
        }).filter(e -> e.getValue() < 0).count());
//        System.out.printf("order=%s%n", order);

//        var counter = 0;
//        for (var ii = 0; ii < order.size(); ii++) {
//            var oi = order.get(ii);
//            var reg = regions.get(oi);
//            var found = false;
//
//            var l = reg.getValue();
//            var stateFigs = Stream.iterate(0, i -> i < l.size(), i -> i + 1).flatMap(i -> Collections.nCopies(l.get(i), i).stream()).toList();
//            System.out.printf("ii=%d, oi=%d, reg=%s, stateFigs=%s%n", ii, oi, reg, stateFigs);
//
//            var state = new ArrayList<Map.Entry<Map.Entry<Integer, Integer>, List<Integer>>>();
//            var next = getFirst(stateFigs.getFirst());
//            for (long li = 0; ; li++) {
//                if (next == null) {
//                    break;
//                }
//
//                var k0 = next.getKey();
//                var fign0 = k0.getKey();
//                var n0 = k0.getValue();
//                var p0 = next.getValue();
//                var p0x = p0.getFirst();
//                var p0y = p0.getLast();
//                var pd0 = allFigsDimensions.get(k0);
//                var d0x = pd0.getFirst();
//                var d0y = pd0.getLast();
//                var p0xd = p0x + d0x;
//                var p0yd = p0y + d0y;
//
//                if (state.stream().anyMatch(t -> {
//                    var k1 = t.getKey();
//                    var fign1 = k1.getKey();
//                    var n1 = k1.getValue();
//                    var p1 = t.getValue();
//                    var p1x = p1.getFirst();
//                    var p1y = p1.getLast();
//                    var pd1 = allFigsDimensions.get(k1);
//                    var d1x = pd1.getFirst();
//                    var d1y = pd1.getLast();
//
//                    return (insideRectangle(p0, p1, pd1) || insideRectangle(List.of(p0xd, p0y), p1, pd1) || insideRectangle(List.of(p0x, p0yd), p1, pd1) || insideRectangle(List.of(p0xd, p0yd), p1, pd1) || insideRectangle(p1, p0, pd0) || insideRectangle(List.of(p1x + d1x, p1y), p0, pd0) || insideRectangle(List.of(p1x, p1y + d1y), p0, pd0) || insideRectangle(List.of(p1x + d1x, p1y + d1y), p0, pd0)) && overlapsMap.computeIfAbsent(Map.entry(Map.entry(Map.entry(fign0, n0), Map.entry(fign1, n1)), List.of(p0x - p1x, p0y - p1y)), k -> allFigs.get(fign1).get(n1).stream().anyMatch(p -> allFigs.get(fign0).get(n0).stream().map(xy -> List.of(xy.getFirst() + p0x, xy.getLast() + p0y)).collect(Collectors.toSet()).contains(List.of(p.getFirst() + p1x, p.getLast() + p1y))));
//                })) for (; next != null; li++) {
//                    if (li % 10_000_000 == 0)
//                        System.out.printf("B %d: state=%s, next=%s%n", li, state, next);
//
//                    var k = next.getKey();
//                    var fign = k.getKey();
//                    var n = k.getValue();
//                    var p = next.getValue();
//                    var x = p.getFirst();
//                    var y = p.getLast();
//                    var d = allFigsDimensions.get(k);
//                    var dx = d.getFirst();
//                    var dy = d.getLast();
//
//                    var limits = reg.getKey();
//                    if ((x = x + dx < limits.getFirst() - 1 ? x + 1 : 0) == 0) {
//                        if ((y = y + dy < limits.getLast() - 1 ? y + 1 : 0) == 0) {
//                            if (++n >= allFigs.get(fign).size()) n = 0;
//                            if (n == 0) next = null;
//                        }
//                    }
//                    if (next != null) {
//                        next = Map.entry(Map.entry(k.getKey(), n), List.of(x, y));
//                        break;
//                    } else if (!state.isEmpty()) next = state.removeLast();
//                }
//                else {
//                    state.add(next);
//
//                    var stateSize = state.size();
//                    if (found = stateSize >= stateFigs.size()) {
//                        counter++;
//
//                        next = null;
//                    } else next = getFirst(stateFigs.get(stateSize));
//                }
//
//                if (next != null) if (li % 10_000_000 == 0)
//                    System.out.printf("A %d: state=%s, next=%s%n", li, state, next);
//            }
//            System.out.printf("counter=%d, reg=%s, state=%s, found=%b%n", counter, reg, state, found);
//
//            if (found) {
//                var regDimentions = reg.getKey();
//
//                Stream.iterate(0, y -> y < regDimentions.getLast(), y -> y + 1).forEach(y -> System.out.println(Stream.iterate(0, x -> x < regDimentions.getFirst(), x -> x + 1).map(x -> Stream.iterate(0, i -> i < state.size(), i -> i + 1).flatMap(i -> {
//                    var e = state.get(i);
//                    var k = e.getKey();
//                    var v = e.getValue();
//                    return allFigs.get(k.getKey()).get(k.getValue()).stream().map(xy -> Map.entry(List.of(xy.getFirst() + v.getFirst(), xy.getLast() + v.getLast()), (char) ('!' + (i % ('~' - ' ')))));
//                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).getOrDefault(List.of(x, y), ' ')).map(String::valueOf).collect(Collectors.joining())));
//            }
//        }
//        System.out.println(counter);
    }

    static Map.Entry<Map.Entry<Integer, Integer>, List<Integer>> getFirst(final int n) {
        return Map.entry(Map.entry(n, 0), List.of(0, 0));
    }

    static boolean insideRectangle(final List<Integer> p, final List<Integer> rectP, final List<Integer> rectD) {
        var px = p.getFirst();
        var py = p.getLast();
        var rx = rectP.getFirst();
        var ry = rectP.getLast();
        return rx <= px && px <= px + rectD.getFirst() && ry <= py && py <= py + rectD.getLast();
    }
}
