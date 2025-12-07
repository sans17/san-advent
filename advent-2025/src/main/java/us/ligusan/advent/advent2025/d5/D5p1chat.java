package us.ligusan.advent.advent2025.d5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class D5p1chat {

    public static void main(String[] args) {

        List<long[]> ranges = new ArrayList<>();
        List<Long> numbers = new ArrayList<>();

        try (var br = new BufferedReader(
                new InputStreamReader(
                        D5p1chat.class.getResourceAsStream("input.txt")
                )
        )) {
            String line;
            boolean inSecondPart = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    inSecondPart = true;
                    continue;
                }

                if (!inSecondPart) {
                    // First part: ranges like "3-5"
                    String[] parts = line.split("-");
                    long start = Long.parseLong(parts[0].trim());
                    long end = Long.parseLong(parts[1].trim());
                    ranges.add(new long[]{start, end});
                } else {
                    // Second part: numbers
                    numbers.add(Long.parseLong(line));
                }
            }

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        long count = 0;

        // For each number, check if it lies in any inclusive range
        outer:
        for (long x : numbers) {
            for (long[] range : ranges) {
                long a = range[0];
                long b = range[1];
                if (x >= a && x <= b) {
                    count++;
                    continue outer;
                }
            }
        }

        System.out.println(count);
    }
}
