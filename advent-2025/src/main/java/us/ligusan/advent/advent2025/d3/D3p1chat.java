package us.ligusan.advent.advent2025.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class D3p1chat {
    public static void main(String[] args) throws Exception {

        long totalSum = 0;

        try (var br = new BufferedReader(
                new InputStreamReader(
                        D3p1chat.class.getResourceAsStream("input.txt")
                )
        )) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() < 2) continue;

                char[] digits = line.toCharArray();
                int n = digits.length;

                int maxDigit = digits[n - 1] - '0';
                int bestVal = -1;

                // Traverse from right to left
                for (int i = n - 2; i >= 0; i--) {
                    int d = digits[i] - '0';
                    int candidate = 10 * d + maxDigit;

                    if (candidate > bestVal) {
                        bestVal = candidate;
                    }

                    if (d > maxDigit) {
                        maxDigit = d;
                    }
                }

                totalSum += bestVal;
            }

            System.out.println(totalSum);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
