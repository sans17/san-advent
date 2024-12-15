package us.ligusan.advent.advent2024.d15;

import java.util.*;

public class D15p1 {
    public static void main(final String[] args) throws Exception {
        var walls = new HashSet<Map.Entry<Integer, Integer>>();
        var boxes = new HashSet<Map.Entry<Integer, Integer>>();

        var position = Map.entry(0, 0);

        var xIndex = 0;
        var yIndex = 0;

        boolean instructionsFlag = false;

        try (var scanner = new Scanner(D15p1.class.getResourceAsStream("input.txt"))) {
            for (String s; scanner.hasNextLine(); ) {
                s = scanner.nextLine();
                System.out.println(s);

                if (instructionsFlag) {
                    for (var c : s.toCharArray()) {
                        int dx = 0;
                        int dy = 0;
                        switch (c) {
                            case '^':
                                dy = -1;
                                break;
                            case '>':
                                dx = 1;
                                break;
                            case 'v':
                                dy = 1;
                                break;
                            case '<':
                                dx = -1;
                                break;
                        }
                        var next = Map.entry(position.getKey() + dx, position.getValue() + dy);
                        var xy = next;
                        for (; boxes.contains(xy); xy = Map.entry(xy.getKey() + dx, xy.getValue() + dy)) ;
                        if (!walls.contains(xy)) {
                            position = next;
                            if (boxes.contains(next)) {
                                boxes.remove(next);
                                boxes.add(xy);
                            }
                        }
                    }
                } else if (s.isEmpty()) {
                    instructionsFlag = true;
                    System.out.format("position=%s, walls=%s, boxes=%s%n", position, walls, boxes);
                } else {
                    for (xIndex = 0; xIndex < s.length(); xIndex++) {
                        var c = s.charAt(xIndex);
                        if (c == '@' || c == '#' || c == 'O') {
                            var e = Map.entry(xIndex, yIndex);
                            if (c == '@') position = e;
                            else (c == '#' ? walls : boxes).add(e);
                        }
                    }
                    yIndex++;
                }
            }
        }
        System.out.format("position=%s, boxes=%s%n", position, boxes);
        for (int y = 0; y < yIndex; y++) {
            for (int x = 0; x < xIndex; x++)
                System.out.print(position.equals(Map.entry(x, y)) ? '@' : walls.contains(Map.entry(x, y)) ? '#' : boxes.contains(Map.entry(x, y)) ? 'O' : '.');
            System.out.println();
        }
        System.out.println(boxes.stream().mapToInt(e -> 100 * e.getValue() + e.getKey()).sum());
    }
}
