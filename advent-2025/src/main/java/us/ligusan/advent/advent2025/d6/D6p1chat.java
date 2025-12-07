package us.ligusan.advent.advent2025.d6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class D6p1chat {

    public static void main(String[] args) {

        List<String> lines = new ArrayList<>();

        // Read all lines from input.txt in resources, preserving spaces
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D6p1chat.class.getResourceAsStream("input.txt")
                )
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);  // do NOT trim; spaces are meaningful
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
        int width = opLine.length();  // all lines have the same length

        // Determine which character positions contain any digit (so they belong to number columns)
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

        // Identify contiguous digit-column blocks: each is a "number column"
        record Col(int start, int end) {}
        List<Col> cols = new ArrayList<>();

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
            cols.add(new Col(start, end));
            c++;
        }

        long totalSum = 0L;

        // Process each number column
        for (Col col : cols) {
            int start = col.start;
            int end = col.end;

            // Operation is in the first character of this column block on the opLine
            char op = opLine.charAt(start);
            if (op != '+' && op != '*') {
                throw new IllegalArgumentException("Unexpected operation '" + op + "' at column " + start);
            }

            long acc = (op == '+') ? 0L : 1L;

            // For each row, extract the number in this column
            for (String row : numberRows) {
                StringBuilder sb = new StringBuilder();
                for (int i = start; i <= end; i++) {
                    char ch = row.charAt(i);
                    if (Character.isDigit(ch)) {
                        sb.append(ch);
                    }
                }

                if (sb.length() == 0) {
                    // no digits in this row for this column: treat as neutral element
                    // + 0 or * 1 → no change
                    continue;
                }

                long value = Long.parseLong(sb.toString());
                if (op == '+') {
                    acc += value;
                } else { // op == '*'
                    acc *= value;
                }
            }

            totalSum += acc;
        }

        System.out.println(totalSum);
    }
}
