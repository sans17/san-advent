package us.ligusan.advent.advent2024.d16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D16p2 {
    public static void main(final String[] args) throws Exception {
        var walls = new HashSet<Map.Entry<Integer, Integer>>();

        Map.Entry<Integer, Integer> end = null;

        List<List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>>> paths = new ArrayList<>();

        var xIndex = 0;
        var yIndex = 0;
        try(var scanner = new Scanner(D16p2.class.getResourceAsStream("input.txt"))) {
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

        var computed = new HashMap<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>, Integer>();
        var allBestPathes = new HashMap<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>, List<List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>>>>();

        List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>> shortestPath = null;
        Integer shortestPathScore = null;

        for(var i = 0; ; i++) {
            paths.sort(Comparator.comparingInt(D16p2::getScore));
            var debugFlag = i % 1000 == 0;
            if(debugFlag) System.out.format("i=%d, paths.size=%d%n", i, paths.size());

            var path = paths.removeFirst();
            if(debugFlag) {
                System.out.println(path);
                System.out.println(getScore(path));
            }

            var position = path.getLast();

            var score = getScore(path);
            if(shortestPathScore != null && score > shortestPathScore) break;

            var bestScore = computed.get(position);
            if(bestScore == null || bestScore.equals(score)) {
                if(bestScore == null) computed.put(position, score);
                allBestPathes.computeIfAbsent(position, _ -> new ArrayList<>()).add(path);
            }
            if(bestScore != null) continue;

            var location = position.getKey();
            if(location.equals(end)) {
                shortestPath = path;
                shortestPathScore = score;
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

        var allBestPathesLocations = shortestPath.stream().flatMap(e -> allBestPathes.get(e).stream().flatMap(l -> l.stream().map(Map.Entry::getKey))).collect(Collectors.toSet());

        System.out.println(shortestPath);
        System.out.println(allBestPathesLocations);

        for(int i = 0; i < yIndex; i++) {
            for(int j = 0; j < xIndex; j++) {
                var l = Map.entry(j, i);
                System.out.print(walls.contains(l) ? '#' : allBestPathesLocations.contains(l) ? 'O' : '.');
            }
            System.out.println();
        }

        System.out.println(allBestPathesLocations.size());
    }

    private static int getScore(final List<Map.Entry<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>>> path) {
        int ret = 0;
        for(int i = 1; i < path.size(); i++) ret += path.get(i).getValue().equals(path.get(i - 1).getValue()) ? 1 : 1000;
        return ret;
    }
}
