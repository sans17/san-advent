package us.ligusan.advent.advent2025.d2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.LongStream;
import java.util.Arrays;

public class D2p2chat {
    public static void main(String[] args) {
        try (InputStream in = D2p2chat.class.getResourceAsStream("input.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String line = reader.readLine();
            if (line == null || line.isBlank()) {
                System.out.println("Input file is empty.");
                return;
            }

            long totalSum = Arrays.stream(line.split(","))
                    .filter(range -> !range.isBlank())
                    .flatMapToLong(range -> {
                        String[] parts = range.trim().split("-");
                        long start = Long.parseLong(parts[0]);
                        long end = Long.parseLong(parts[1]);
                        return LongStream.rangeClosed(start, end);
                    })
                    .filter(D2p2chat::isMadeOfRepeatedSubstring)
                    .peek(System.out::println) // Print each match
                    .sum();

            System.out.println("Total sum of numbers made of repeated substrings: " + totalSum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if the number is made of a substring repeated multiple times
    private static boolean isMadeOfRepeatedSubstring(long num) {
        String s = Long.toString(num);
        int len = s.length();

        for (int subLen = 1; subLen <= len / 2; subLen++) {
            if (len % subLen != 0) continue;

            String part = s.substring(0, subLen);
            int repetitions = len / subLen;
            StringBuilder repeated = new StringBuilder();
            for (int i = 0; i < repetitions; i++) {
                repeated.append(part);
            }
            if (repeated.toString().equals(s)) {
                return true;
            }
        }
        return false;
    }
}