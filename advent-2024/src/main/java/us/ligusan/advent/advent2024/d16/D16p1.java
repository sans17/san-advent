package us.ligusan.advent.advent2024.d16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class D16p1 {
    public static void main(final String[] args) throws Exception {
        var walls = new HashSet<Map.Entry<Integer, Integer>>();

        Map.Entry<Integer, Integer> end = null;

        List<List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>>> paths = new ArrayList<>();

        var xIndex = 0;
        var yIndex = 0;
        try(var scanner = new Scanner(D16p1.class.getResourceAsStream("input.txt"))) {
            for(String s; scanner.hasNextLine(); ) {
                s = scanner.nextLine();
                System.out.println(s);

                for(xIndex = 0; xIndex < s.length(); xIndex++) {
                    var c = s.charAt(xIndex);
                    if(c != '.') {
                        var e = Map.entry(xIndex, yIndex);
                        if(c == 'E') end = e;
                        else if(c == '#') walls.add(e);
                        else paths.add(new ArrayList<>(List.of(Map.entry(e, Map.entry(1, 0)))));
                    }
                }
                yIndex++;
            }
        }
        System.out.format("end=%s, walls=%s, paths=%s%n", end, walls, paths);

        var computed = new HashSet<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>>();

        List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>> shortestPath;

        for(var i = 0; ; i++) {
            paths.sort(Comparator.comparingInt(D16p1::getScore));
            var debugFlag = i % 1000 == 0;
            if(debugFlag) System.out.format("i=%d, paths.size=%d%n", i, paths.size());

            var path = paths.removeFirst();
            if(debugFlag) {
                System.out.println(path);
                System.out.println(getScore(path));
            }

            var position = path.getLast();
            if(computed.contains(position)) continue;
            computed.add(position);

            var location = position.getKey();
            if(location.equals(end)) {
                shortestPath = path;
                break;
            }

            var x = location.getKey();
            var y = location.getValue();
            var direction = position.getValue();
            var dx = direction.getKey();

            var next = Map.entry(x + dx, y + direction.getValue());
            if(!walls.contains(next)) {
                var newPath = new ArrayList<>(path);
                newPath.add(Map.entry(next, direction));
                paths.addFirst(newPath);
            }

            Stream.of(-1, 1).map(d -> dx == 0 ? Map.entry(d, 0) : Map.entry(0, d)).filter(d -> {
                var nextLocation = Map.entry(x + d.getKey(), y + d.getValue());
                return !walls.contains(nextLocation) && path.stream().map(Map.Entry::getKey).noneMatch(nextLocation::equals);
            }).map(d -> {
                var newPath = new ArrayList<>(path);
                newPath.add(Map.entry(location, d));
                return newPath;
            }).forEach(paths::add);
        }
        System.out.println(shortestPath);
        System.out.println(getScore(shortestPath));

        for(int i = 0; i < yIndex; i++) {
            for(int j = 0; j < xIndex; j++) {
                var l = Map.entry(j, i);
                System.out.print(walls.contains(l) ? '#' : shortestPath.stream().map(Map.Entry::getKey).anyMatch(l::equals) ? 'O' : '.');
            }
            System.out.println();
        }
    }

    private static int getScore(final List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>> path) {
        int ret = 0;
        for(int i = 1; i < path.size(); i++) ret += path.get(i).getValue().equals(path.get(i - 1).getValue()) ? 1 : 1000;
        return ret;
    }
}
