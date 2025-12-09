package us.ligusan.advent.advent2025.d8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.math.BigInteger;

public class D8p1chat {

    record Point(long x, long y, long z) {}

    record Pair(int a, int b, double dist) {}

    static class DSU {
        int[] parent, size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }

        int sizeOf(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {

        List<Point> points = new ArrayList<>();

        // Read input points
        try (var br = new BufferedReader(
                new InputStreamReader(
                        D8p1chat.class.getResourceAsStream("input.txt")
                ))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                long x = Long.parseLong(parts[0].trim());
                long y = Long.parseLong(parts[1].trim());
                long z = Long.parseLong(parts[2].trim());
                points.add(new Point(x, y, z));
            }

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return;
        }

        int n = points.size();
        if (n == 0) {
            System.out.println(0);
            return;
        }

        // Build all point pairs with distances
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

        // Sort pairs by Euclidean distance
        pairs.sort(Comparator.comparingDouble(p -> p.dist));

        // Union–Find structure
        DSU dsu = new DSU(n);

        // Perform exactly 10 link operations
        int links = Math.min(1000, pairs.size());
        for (int i = 0; i < links; i++) {
            Pair p = pairs.get(i);
            dsu.union(p.a(), p.b());
        }
        

        // Count sizes of all connected components
        Map<Integer, Integer> compSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            compSizes.put(root, compSizes.getOrDefault(root, 0) + 1);
        }

        // Sort component sizes descending
        List<Integer> sizes = new ArrayList<>(compSizes.values());
        sizes.sort(Collections.reverseOrder());

        // If fewer than 3 sets exist, pad with 1s (safe fallback)
        while (sizes.size() < 3) sizes.add(1);

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < 3; i++) {
            result = result.multiply(BigInteger.valueOf(sizes.get(i)));
        }

        // Output product of 3 largest components
        System.out.println(result);
    }
}
