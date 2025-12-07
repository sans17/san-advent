package us.ligusan.advent.advent2025.d7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class D7p2chat {

    public static void main(String[] args) {

        List<String> rows = new ArrayList<>();

        // Read grid
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D7p2chat.class.getResourceAsStream("input.txt")
                ))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        int height = rows.size();
        int width = rows.get(0).length();

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
            System.out.println("0");
            return;
        }

        // DP table: dp[r][c] = number of ways to reach (r,c)
        BigInteger[][] dp = new BigInteger[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                dp[i][j] = BigInteger.ZERO;

        dp[startRow][startCol] = BigInteger.ONE;

        // Process rows from startRow downward
        for (int r = startRow; r < height - 1; r++) {
            for (int c = 0; c < width; c++) {

                if (dp[r][c].equals(BigInteger.ZERO))
                    continue;

                int nr = r + 1;
                int nc = c;

                char cell = rows.get(nr).charAt(nc);

                if (cell == '.' || cell == 'S') {
                    // continue straight down
                    dp[nr][nc] = dp[nr][nc].add(dp[r][c]);

                } else if (cell == '^') {
                    // split into two paths
                    BigInteger val = dp[r][c];

                    if (nc - 1 >= 0) {
                        dp[nr][nc - 1] = dp[nr][nc - 1].add(val);
                    }
                    if (nc + 1 < width) {
                        dp[nr][nc + 1] = dp[nr][nc + 1].add(val);
                    }
                } else {
                    // treat any unexpected char as empty
                    dp[nr][nc] = dp[nr][nc].add(dp[r][c]);
                }
            }
        }

        // Sum all paths reaching the last row
        BigInteger total = BigInteger.ZERO;
        for (int c = 0; c < width; c++) {
            total = total.add(dp[height - 1][c]);
        }

        System.out.println(total);
    }
}
