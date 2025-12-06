package us.ligusan.advent.advent2025.d1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class D1p2chat {
    public static void main(String[] args) {

        int position = 50;       // starting position on the dial (0–99)
        long countZero = 0L;     // count of clicks landing exactly on 0

        try (var br = new BufferedReader(
                new InputStreamReader(
                        D1p2chat.class.getResourceAsStream("input.txt")
                )
        )) {

            String line;
            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                var direction = line.charAt(0);              // 'L' or 'R'
                long distance = Long.parseLong(line.substring(1));

                // Calculate first step at which we hit 0
                long firstHitStep = switch (direction) {
                    case 'R' -> (position == 0) ? 100L : (100L - position);
                    case 'L' -> (position == 0) ? 100L : position;
                    default -> throw new IllegalArgumentException(
                            "Unexpected direction: " + direction
                    );
                };

                // Count number of 0-hits in this rotation
                if (distance >= firstHitStep) {
                    countZero += 1L + (distance - firstHitStep) / 100L;
                }

                // Update final dial position after rotation
                long move = distance % 100L;

                position = switch (direction) {
                    case 'R' -> (int) ((position + move) % 100L);
                    case 'L' -> (int) ((position - move + 100L) % 100L);
                    default -> position;  // unreachable due to earlier validation
                };
            }

            System.out.println(countZero);

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}