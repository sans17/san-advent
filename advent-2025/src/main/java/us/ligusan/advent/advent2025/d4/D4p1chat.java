package us.ligusan.advent.advent2025.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class D4p1chat {

    public static void main(String[] args) {

        List<String> rows = new ArrayList<>();

        try (var br = new BufferedReader(
                new InputStreamReader(
                        D4p1chat.class.getResourceAsStream("input.txt")
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
        for (int i = 0; i < height; i++) {
            grid[i] = rows.get(i).toCharArray();
        }

        int result = 0;

        // Now we consider ALL cells, including borders
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] != '@') continue;

                int neighborAtCount = 0;

                // Check all 8 neighboring positions, but stay in bounds
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;

                        int nr = r + dr;
                        int nc = c + dc;

                        if (nr < 0 || nr >= height || nc < 0 || nc >= width) {
                            continue;
                        }

                        if (grid[nr][nc] == '@') {
                            neighborAtCount++;
                        }
                    }
                }

                if (neighborAtCount < 4) {
                    result++;
                }
            }
        }

        System.out.println(result);
    }
}
