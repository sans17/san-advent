package us.ligusan.advent.advent2024.d15;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D15p2 {
    private static final Set<Map.Entry<Integer, Integer>> WALLS = new HashSet<>();
    private static final Map<Map.Entry<Integer, Integer>, Character> BOXES = new HashMap<>();

    private static Map.Entry<Integer, Integer> position = Map.entry(0, 0);

    private static int xIndex = 0;
    private static int yIndex = 0;

    public static void main(final String[] args) throws Exception {
        boolean instructionsFlag = false;

        try (var scanner = new Scanner(D15p2.class.getResourceAsStream("input.txt"))) {
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

                        final var y = position.getValue();
                        var next = Map.entry(position.getKey() + dx, y + dy);
                        final var dxFinal = dx;
                        final var dyFinal = dy;

                        boolean wallFlag;
                        var boxesToMove = new HashMap<Map.Entry<Integer, Integer>, Character>();
                        for (var edge = Collections.singleton(next); !(wallFlag = edge.stream().anyMatch(WALLS::contains)); ) {
                            var newBoxesToMove = edge.stream().flatMap(e ->
                            {
                                var boxC = BOXES.remove(e);
                                if (boxC == null) return Stream.empty();
                                else {
                                    var e1 = Map.entry(e.getKey() + (boxC.equals('[') ? 1 : -1), e.getValue());
                                    return Stream.of(Map.entry(e, boxC), Map.entry(e1, BOXES.remove(e1)));
                                }
                            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                            if (newBoxesToMove.isEmpty()) break;

                            boxesToMove.putAll(newBoxesToMove);
                            edge = switch (c) {
                                case '^', 'v' ->
                                        newBoxesToMove.keySet().stream().map(e -> Map.entry(e.getKey() + dxFinal, e.getValue() + dyFinal)).collect(Collectors.toSet());
                                case '>', '<' -> {
                                    var xStream = newBoxesToMove.keySet().stream().mapToInt(Map.Entry::getKey);
                                    yield Collections.singleton(Map.entry((c == '>' ? xStream.max() : xStream.min()).getAsInt() + dxFinal, y));
                                }
                                default -> throw new IllegalStateException("Unexpected value: " + c);
                            };
                        }
                        BOXES.putAll(wallFlag ? boxesToMove : boxesToMove.entrySet().stream().collect(Collectors.toMap(e -> Map.entry(e.getKey().getKey() + dxFinal, e.getKey().getValue() + dyFinal), Map.Entry::getValue)));
                        if (!wallFlag) position = next;

//                        System.out.println(c);
//                        printMap();
                    }
                } else if (s.isEmpty()) {
                    instructionsFlag = true;
                    System.out.format("position=%s, walls=%s, boxes=%s%n", position, WALLS, BOXES);
                    printMap();
                } else {
                    for (xIndex = 0; xIndex < s.length(); xIndex++) {
                        var c = s.charAt(xIndex);
                        if (c == '@' || c == '#' || c == 'O') {
                            var e = Map.entry(xIndex * 2, yIndex);
                            if (c == '@') position = e;
                            else {
                                var e1 = Map.entry(xIndex * 2 + 1, yIndex);
                                if (c == '#') {
                                    WALLS.add(e);
                                    WALLS.add(e1);
                                } else {
                                    BOXES.put(e, '[');
                                    BOXES.put(e1, ']');
                                }
                            }
                        }
                    }
                    yIndex++;
                }
            }
        }
        System.out.format("position=%s, boxes=%s%n", position, BOXES);
        printMap();
        System.out.println(BOXES.entrySet().stream().filter(e -> e.getValue().equals('[')).map(Map.Entry::getKey).mapToInt(e -> 100 * e.getValue() + e.getKey()).sum());
    }

    private static void printMap() {
        for (int y = 0; y < yIndex; y++) {
            for (int x = 0; x < xIndex * 2; x++)
                System.out.print(position.equals(Map.entry(x, y)) ? '@' : WALLS.contains(Map.entry(x, y)) ? '#' : BOXES.getOrDefault(Map.entry(x, y), '.'));
            System.out.println();
        }
    }
}
