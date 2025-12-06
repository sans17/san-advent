package us.ligusan.advent.advent2025.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class D3p2chat {

    private static final int K = 12;   // number of digits to pick

    public static void main(String[] args) {

        BigInteger totalSum = BigInteger.ZERO;

        try (var br = new BufferedReader(
                new InputStreamReader(
                        D3p2chat.class.getResourceAsStream("input.txt")
                )
        )) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() < K) continue;   // skip invalid lines

                String best = selectBest12Digits(line);
                totalSum = totalSum.add(new BigInteger(best));
            }

            System.out.println(totalSum);

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    // pick lexicographically largest subsequence of length 12
    private static String selectBest12Digits(String s) {
        int n = s.length();
        int remaining = K;
        int start = 0;

        StringBuilder result = new StringBuilder(K);

        while (remaining > 0) {
            int maxDigit = -1;
            int maxPos = start;

            // search window: must leave enough characters for the remainder
            int end = n - remaining;

            for (int i = start; i <= end; i++) {
                int d = s.charAt(i) - '0';
                if (d > maxDigit) {
                    maxDigit = d;
                    maxPos = i;

                    // if digit is 9, cannot do better
                    if (d == 9) break;
                }
            }

            result.append((char) ('0' + maxDigit));
            start = maxPos + 1;
            remaining--;
        }

        return result.toString();
    }
}