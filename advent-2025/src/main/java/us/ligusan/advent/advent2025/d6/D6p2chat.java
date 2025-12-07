package us.ligusan.advent.advent2025.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class D6p2chat {

    public static void main(String[] args) {

        List<String> lines = new ArrayList<>();

        // Read all lines from input.txt (preserve spaces)
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D6p2chat.class.getResourceAsStream("input.txt")
                )
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                // do NOT trim; spaces are meaningful
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        if (lines.size() < 2) {
            System.out.println(0);
            return;
        }

        String opLine = lines.getLast();
        List<String> numberRows = lines.subList(0, lines.size() - 1);

        int height = numberRows.size();
        int width = opLine.length();   // all lines have the same length

        // Mark which columns contain any digit in the number rows
        boolean[] isDigitCol = new boolean[width];
        for (int c = 0; c < width; c++) {
            boolean anyDigit = false;
            for (String row : numberRows) {
                char ch = row.charAt(c);
                if (Character.isDigit(ch)) {
                    anyDigit = true;
                    break;
                }
            }
            isDigitCol[c] = anyDigit;
        }

        // Find contiguous groups of digit columns
        record ColGroup(int start, int end) {}
        List<ColGroup> groups = new ArrayList<>();

        int c = 0;
        while (c < width) {
            if (!isDigitCol[c]) {
                c++;
                continue;
            }
            int start = c;
            while (c + 1 < width && isDigitCol[c + 1]) {
                c++;
            }
            int end = c;
            groups.add(new ColGroup(start, end));
            c++;
        }

        long totalSum = 0L;

        for (ColGroup g : groups) {
            int start = g.start;
            int end = g.end;

            // Operation from the first column of this group on the op line
            char op = opLine.charAt(start);
            if (op != '+' && op != '*') {
                throw new IllegalArgumentException(
                        "Unexpected operation '" + op + "' at column " + start
                );
            }

            long acc = (op == '+') ? 0L : 1L;

            // For each column in this group, build a vertical number
            for (int col = start; col <= end; col++) {
                // Find first row with a digit in this column
                int firstDigitRow = -1;
                for (int row = 0; row < height; row++) {
                    char ch = numberRows.get(row).charAt(col);
                    if (Character.isDigit(ch)) {
                        firstDigitRow = row;
                        break;
                    }
                }
                if (firstDigitRow == -1) {
                    continue; // no digits at all in this column
                }

                StringBuilder sb = new StringBuilder();
                for (int row = firstDigitRow; row < height; row++) {
                    char ch = numberRows.get(row).charAt(col);
                    if (Character.isDigit(ch)) {
                        sb.append(ch);
                    } else {
                        break; // stop at first non-digit below
                    }
                }

                if (sb.length() == 0) continue;

                long value = Long.parseLong(sb.toString());
                if (op == '+') {
                    acc += value;
                } else { // '*'
                    acc *= value;
                }
            }

            totalSum += acc;
        }

        System.out.println(totalSum);
    }
}
