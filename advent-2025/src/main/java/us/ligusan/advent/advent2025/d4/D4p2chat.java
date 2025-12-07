package us.ligusan.advent.advent2025.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class D4p2chat {

    public static void main(String[] args) {

        List<String> rows = new ArrayList<>();

        // Read grid from input.txt as resource
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D4p2chat.class.getResourceAsStream("input.txt")
                )
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (!line.isEmpty()) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        if (rows.isEmpty()) {
            System.out.println(0);
            return;
        }

        int height = rows.size();
        int width = rows.getFirst().length();

        char[][] grid = new char[height][width];
        for (int r = 0; r < height; r++) {
            grid[r] = rows.get(r).toCharArray();
        }

        long totalReplaced = 0L;

        // Evolve until no changes occur in a step
        while (true) {
            boolean changed = false;
            char[][] next = new char[height][width];

            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {

                    if (grid[r][c] != '@') {
                        // stays '.' (or any non-@ char)
                        next[r][c] = grid[r][c];
                        continue;
                    }

                    int neighborAtCount = countAtNeighbors(grid, r, c, height, width);

                    if (neighborAtCount < 4) {
                        // this '@' gets replaced
                        next[r][c] = '.';
                        totalReplaced++;
                        changed = true;
                    } else {
                        // survives as '@'
                        next[r][c] = '@';
                    }
                }
            }

            if (!changed) {
                break;  // reached final position
            }

            grid = next;  // move to next step
        }

        System.out.println(totalReplaced);
    }

    private static int countAtNeighbors(char[][] grid, int r, int c, int height, int width) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int nr = r + dr;
                int nc = c + dc;

                if (nr < 0 || nr >= height || nc < 0 || nc >= width) {
                    continue;
                }

                if (grid[nr][nc] == '@') {
                    count++;
                }
            }
        }
        return count;
    }
}
