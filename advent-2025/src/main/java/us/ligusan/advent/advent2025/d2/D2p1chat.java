package us.ligusan.advent.advent2025.d2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.LongStream;
import java.util.Arrays;

public class D2p1chat {
    public static void main(String[] args) {
        try (InputStream in = D2p1chat.class.getResourceAsStream("input.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            // Read the single line from the file
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
                    .filter(D2p1chat::isRepeatedTwice)
                    .peek(System.out::println) // Print matching numbers
                    .sum();

            System.out.println("Total sum of repeated-twice numbers: " + totalSum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check if number looks like "XYXY", i.e. repeated halves
    private static boolean isRepeatedTwice(long num) {
        String s = Long.toString(num);
        int len = s.length();
        if (len % 2 != 0) return false;
        String first = s.substring(0, len / 2);
        String second = s.substring(len / 2);
        return first.equals(second);
    }
}