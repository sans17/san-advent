package us.ligusan.advent.advent2025.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.math.BigInteger;

public class D8p2chat {

    record Point(long x, long y, long z) {}
    record Pair(int a, int b, double dist) {}

    static class DSU {
        int[] parent, size;
        int components;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;

            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }

            components--;
            return true;
        }
    }

    public static void main(String[] args) {

        List<Point> points = new ArrayList<>();

        // Read points
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D8p2chat.class.getResourceAsStream("input.txt")
                )
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                points.add(new Point(
                        Long.parseLong(p[0].trim()),
                        Long.parseLong(p[1].trim()),
                        Long.parseLong(p[2].trim())
                ));
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        int n = points.size();
        if (n < 2) {
            System.out.println(0);
            return;
        }

        // Create all pairs
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Point a = points.get(i);
            for (int j = i + 1; j < n; j++) {
                Point b = points.get(j);

                double dist = Math.sqrt(
                        (a.x - b.x) * (a.x - b.x) +
                                (a.y - b.y) * (a.y - b.y) +
                                (a.z - b.z) * (a.z - b.z)
                );

                pairs.add(new Pair(i, j, dist));
            }
        }

        // Sort by distance
        pairs.sort(Comparator.comparingDouble(p -> p.dist));

        DSU dsu = new DSU(n);

        int lastA = -1, lastB = -1;

        // Perform Kruskal until fully connected
        for (Pair p : pairs) {
            if (dsu.union(p.a(), p.b())) {
                lastA = p.a();
                lastB = p.b();
            }
            if (dsu.components == 1) break;
        }

        // Output product of X-coordinates of last merged points
        Point A = points.get(lastA);
        Point B = points.get(lastB);

        BigInteger result = BigInteger.valueOf(A.x)
                .multiply(BigInteger.valueOf(B.x));

        System.out.println(result);
    }
}
