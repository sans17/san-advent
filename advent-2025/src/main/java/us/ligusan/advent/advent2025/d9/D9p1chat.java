package us.ligusan.advent.advent2025.d9;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class D9p1chat {

    static class Pt {
        final long x, y;
        Pt(long x, long y) { this.x = x; this.y = y; }
        @Override public String toString() { return x + "," + y; }
    }

    public static void main(String[] args) throws Exception {
        List<Pt> pts = readPointsFromResource("input.txt");

        long bestArea = Long.MIN_VALUE;
        int bestI = -1, bestJ = -1;

        for (int i = 0; i < pts.size(); i++) {
            Pt a = pts.get(i);
            for (int j = i + 1; j < pts.size(); j++) {
                Pt b = pts.get(j);

                long widthInclusive  = Math.abs(a.x - b.x) + 1;
                long heightInclusive = Math.abs(a.y - b.y) + 1;
                long area = widthInclusive * heightInclusive;

                if (area > bestArea) {
                    bestArea = area;
                    bestI = i;
                    bestJ = j;
                }
            }
        }

        // If 0 or 1 point, define max area as 0
        if (bestI == -1) {
            System.out.println(0);
            return;
        }

        System.out.println(bestArea);

        // Optional debug:
        // System.err.println("Best pair: " + pts.get(bestI) + " and " + pts.get(bestJ));
    }

    private static List<Pt> readPointsFromResource(String resourceName) throws IOException {
        InputStream is = D9p1chat.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new FileNotFoundException("Resource not found on classpath: " + resourceName);
        }

        List<Pt> pts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 2) throw new IllegalArgumentException("Bad line: " + line);

                long x = Long.parseLong(parts[0].trim());
                long y = Long.parseLong(parts[1].trim());
                pts.add(new Pt(x, y));
            }
        }
        return pts;
    }
}
