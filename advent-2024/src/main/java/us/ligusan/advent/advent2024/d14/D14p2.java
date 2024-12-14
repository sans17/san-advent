package us.ligusan.advent.advent2024.d14;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class D14p2 {
    public static void main(final String[] args) throws Exception {
        var xSize = 101;
        var ySize = 103;
        var file = "input.txt";

        var positions = new ArrayList<Map.Entry<Integer, Integer>>();
        var velocities = new ArrayList<Map.Entry<Integer, Integer>>();

        try (var scanner = new Scanner(D14p2.class.getResourceAsStream(file))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.println(s);

                var m = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)").matcher(s);
                m.find();
                positions.add(Map.entry(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
                velocities.add(Map.entry(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
            });
        }
        System.out.println(positions);
        System.out.println(velocities);

        for (int i = 0, limit = 1; limit < 9; i++) {
            var limitFinal = limit;
            if (positions.stream().mapToInt(p -> {
                for (int yStart = p.getValue(), y = yStart; ; )
                    if (!positions.contains(Map.entry(p.getKey(), ++y))) return y - yStart;
            }).anyMatch(l -> l >= limitFinal)) {
                System.out.format("i=%d, limit=%d%n", i, limit);
                limit++;

                for (int y = 0; y < ySize; y++) {
                    var finalY = y;
                    for (int x = 0; x < xSize; x++) {
                        var finalX = x;
                        System.out.print(positions.stream().anyMatch(p -> p.getKey() == finalX && p.getValue() == finalY) ? '#' : ' ');
                    }
                    System.out.println();
                }
                System.out.println();
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }

            for (int j = 0; j < positions.size(); j++) {
                var p = positions.get(j);
                var v = velocities.get(j);

                var newX = (p.getKey() + v.getKey()) % xSize;
                if (newX < 0) newX += xSize;
                var newY = (p.getValue() + v.getValue()) % ySize;
                if (newY < 0) newY += ySize;

                positions.set(j, Map.entry(newX, newY));
            }
        }
    }
}
