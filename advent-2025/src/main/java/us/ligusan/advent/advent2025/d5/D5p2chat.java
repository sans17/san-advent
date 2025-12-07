package us.ligusan.advent.advent2025.d5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class D5p2chat {

    public static void main(String[] args) {

        List<long[]> ranges = new ArrayList<>();

        try (var br = new BufferedReader(
                new InputStreamReader(
                        D5p2chat.class.getResourceAsStream("input.txt")
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
                    if (end < start) {
                        long tmp = start;
                        start = end;
                        end = tmp;
                    }
                    ranges.add(new long[]{start, end});
                } else {
                    // Second part: ignore these lines entirely
                    // (but we still read them to exhaust the stream)
                }
            }

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        if (ranges.isEmpty()) {
            System.out.println(0);
            return;
        }

        // Sort ranges by start, then by end
        ranges.sort(Comparator.comparingLong((long[] r) -> r[0])
                .thenComparingLong(r -> r[1]));

        long totalDistinct = 0L;

        long currentStart = ranges.get(0)[0];
        long currentEnd   = ranges.get(0)[1];

        for (int i = 1; i < ranges.size(); i++) {
            long[] r = ranges.get(i);
            long start = r[0];
            long end   = r[1];

            if (start > currentEnd + 1) {
                // Disjoint gap: finalize previous merged range
                totalDistinct += (currentEnd - currentStart + 1);
                currentStart = start;
                currentEnd = end;
            } else {
                // Overlapping or touching: extend current range
                if (end > currentEnd) {
                    currentEnd = end;
                }
            }
        }

        // Add last merged range
        totalDistinct += (currentEnd - currentStart + 1);

        System.out.println(totalDistinct);
    }
}
