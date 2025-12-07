package us.ligusan.advent.advent2025.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D7p1chat {

    public static void main(String[] args) {

        List<String> rows = new ArrayList<>();

        // Read grid
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D7p1chat.class.getResourceAsStream("input.txt")
                )
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line); // do not trim
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        int height = rows.size();
        int width = rows.getFirst().length();

        // Find S
        int startRow = -1, startCol = -1;
        for (int r = 0; r < height; r++) {
            int c = rows.get(r).indexOf('S');
            if (c >= 0) {
                startRow = r;
                startCol = c;
                break;
            }
        }

        if (startRow == -1) {
            System.out.println(0);
            return;
        }

        // Active beams represented as unique columns in current row
        Set<Integer> beams = new HashSet<>();
        beams.add(startCol);

        long splitCount = 0;

        // Simulate row by row
        for (int r = startRow; r + 1 < height; r++) {

            Set<Integer> nextBeams = new HashSet<>();

            for (int col : beams) {
                int nr = r + 1;  // next row
                int nc = col;    // same column unless split

                char cell = rows.get(nr).charAt(nc);

                if (cell == '^') {
                    // Split
                    splitCount++;

                    // down-left
                    if (nc - 1 >= 0) nextBeams.add(nc - 1);

                    // down-right
                    if (nc + 1 < width) nextBeams.add(nc + 1);

                } else {
                    // normal downward movement
                    nextBeams.add(nc);
                }
            }

            // beams automatically merged by using a Set
            beams = nextBeams;

            if (beams.isEmpty()) break;
        }

        System.out.println(splitCount);
    }
}
