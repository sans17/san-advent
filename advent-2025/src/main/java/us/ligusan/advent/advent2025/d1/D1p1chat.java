package us.ligusan.advent.advent2025.d1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class D1p1chat {
    public static void main(String[] args) {
        int position = 50;      // initial position
        int countZero = 0;      // how many times we land on 0

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(D1p1chat.class.getResourceAsStream("input.txt")));

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // skip empty lines
                }

                char direction = line.charAt(0); // 'L' or 'R'
                int distance = Integer.parseInt(line.substring(1).trim());

                if (direction == 'L' || direction == 'l') {
                    // Left = towards lower numbers
                    position = (position - distance) % 100;
                    if (position < 0) {
                        position += 100;  // fix negative modulo
                    }
                } else if (direction == 'R' || direction == 'r') {
                    // Right = towards higher numbers
                    position = (position + distance) % 100;
                } else {
                    // Invalid line format; you could throw an error instead
                    System.err.println("Ignoring invalid instruction: " + line);
                    continue;
                }

                // Check if dial points at 0 after this rotation
                if (position == 0) {
                    countZero++;
                }
            }

            System.out.println(countZero);

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
