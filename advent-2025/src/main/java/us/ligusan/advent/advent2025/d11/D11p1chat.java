package us.ligusan.advent.advent2025.d11;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Counts number of directed paths from node "you" to node "out".
 * Input lines: "node: space-separated neighbors"
 *
 * Assumptions:
 * - Graph is directed as written.
 * - If the graph contains a cycle reachable from "you" that can also reach "out",
 *   then there are infinitely many distinct paths; this program prints "INF".
 * - Otherwise prints the finite path count as a BigInteger.
 */
public class D11p1chat {

    public static void main(String[] args) throws Exception {
        InputStream is = D11p1chat.class.getResourceAsStream("input.txt");
        if (is == null) is = System.in;

        Map<String, List<String>> g = new HashMap<>();
        Set<String> all = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(":");
                if (parts.length != 2) throw new IllegalArgumentException("Bad line: " + line);

                String u = parts[0].trim();
                String rhs = parts[1].trim();

                all.add(u);

                List<String> nbrs = new ArrayList<>();
                if (!rhs.isEmpty()) {
                    for (String v : rhs.split("\\s+")) {
                        if (v.isEmpty()) continue;
                        nbrs.add(v);
                        all.add(v);
                    }
                }
                g.put(u, nbrs);
            }
        }

        // Ensure every node exists in adjacency map
        for (String v : all) g.putIfAbsent(v, List.of());

        String start = "you";
        String target = "out";

        if (!g.containsKey(start) || !g.containsKey(target)) {
            System.out.println(0);
            return;
        }

        // Reachability pruning
        Set<String> fromStart = reachable(g, start);
        Map<String, List<String>> rev = reverse(g);
        Set<String> toTarget = reachable(rev, target);

        // Consider only nodes that are on some start->...->target corridor
        Set<String> corridor = new HashSet<>(fromStart);
        corridor.retainAll(toTarget);

        if (!corridor.contains(start) || !corridor.contains(target)) {
            System.out.println(0);
            return;
        }

        // Detect cycle in corridor reachable from start and able to reach target -> infinite paths
        if (hasCycleInSubgraph(g, corridor)) {
            System.out.println("INF");
            return;
        }

        // DAG DP with memoization
        Map<String, java.math.BigInteger> memo = new HashMap<>();
        java.math.BigInteger ans = countPathsDAG(g, corridor, start, target, memo);
        System.out.println(ans);
    }

    private static java.math.BigInteger countPathsDAG(
            Map<String, List<String>> g,
            Set<String> corridor,
            String u,
            String target,
            Map<String, java.math.BigInteger> memo
    ) {
        if (!corridor.contains(u)) return java.math.BigInteger.ZERO;
        if (u.equals(target)) return java.math.BigInteger.ONE;
        java.math.BigInteger cached = memo.get(u);
        if (cached != null) return cached;

        java.math.BigInteger sum = java.math.BigInteger.ZERO;
        for (String v : g.getOrDefault(u, List.of())) {
            if (!corridor.contains(v)) continue;
            sum = sum.add(countPathsDAG(g, corridor, v, target, memo));
        }
        memo.put(u, sum);
        return sum;
    }

    private static Set<String> reachable(Map<String, List<String>> g, String start) {
        Set<String> vis = new HashSet<>();
        ArrayDeque<String> q = new ArrayDeque<>();
        vis.add(start);
        q.add(start);
        while (!q.isEmpty()) {
            String u = q.poll();
            for (String v : g.getOrDefault(u, List.of())) {
                if (vis.add(v)) q.add(v);
            }
        }
        return vis;
    }

    private static Map<String, List<String>> reverse(Map<String, List<String>> g) {
        Map<String, List<String>> rev = new HashMap<>();
        for (String u : g.keySet()) rev.putIfAbsent(u, new ArrayList<>());
        for (Map.Entry<String, List<String>> e : g.entrySet()) {
            String u = e.getKey();
            for (String v : e.getValue()) {
                rev.putIfAbsent(v, new ArrayList<>());
                rev.get(v).add(u);
            }
        }
        return rev;
    }

    // Cycle detection in induced subgraph over "corridor" nodes
    private static boolean hasCycleInSubgraph(Map<String, List<String>> g, Set<String> corridor) {
        // 0=unseen, 1=visiting, 2=done
        Map<String, Integer> color = new HashMap<>();
        for (String v : corridor) color.put(v, 0);

        for (String v : corridor) {
            if (color.get(v) == 0) {
                if (dfsCycle(g, corridor, v, color)) return true;
            }
        }
        return false;
    }

    private static boolean dfsCycle(Map<String, List<String>> g, Set<String> corridor, String u, Map<String, Integer> color) {
        color.put(u, 1);
        for (String v : g.getOrDefault(u, List.of())) {
            if (!corridor.contains(v)) continue;
            int cv = color.getOrDefault(v, 0);
            if (cv == 1) return true;
            if (cv == 0 && dfsCycle(g, corridor, v, color)) return true;
        }
        color.put(u, 2);
        return false;
    }
}
