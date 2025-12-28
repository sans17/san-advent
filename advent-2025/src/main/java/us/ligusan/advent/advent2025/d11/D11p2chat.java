package us.ligusan.advent.advent2025.d11;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.math.BigInteger;

/**
 * Count number of directed paths from "svr" to "out" that visit both "dac" and "fft" (any order).
 *
 * Input:
 *   node: space-separated neighbors
 *
 * Output:
 *   finite count as BigInteger, or "INF" if infinitely many paths exist.
 *
 * Infinite rule:
 *   If the subgraph that is reachable from start and can reach target contains a directed cycle,
 *   then paths are infinite (because walks are unbounded).
 */
public class D11p2chat {

    public static void main(String[] args) throws Exception {
        InputStream is = D11p2chat.class.getResourceAsStream("input.txt");
        if (is == null) is = System.in;

        Map<String, List<String>> g = new HashMap<>();
        Set<String> all = new HashSet<>();
        Pattern ws = Pattern.compile("\\s+");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(":", 2);
                if (parts.length != 2) throw new IllegalArgumentException("Bad line: " + line);

                String u = parts[0].trim();
                String rhs = parts[1].trim();
                all.add(u);

                List<String> nbrs = new ArrayList<>();
                if (!rhs.isEmpty()) {
                    for (String v : ws.split(rhs)) {
                        if (v.isEmpty()) continue;
                        nbrs.add(v);
                        all.add(v);
                    }
                }
                g.put(u, nbrs);
            }
        }

        // Ensure all nodes are present in adjacency
        for (String v : all) g.putIfAbsent(v, List.of());

        String start = "svr";
        String target = "out";
        String req1 = "dac";
        String req2 = "fft";

        if (!g.containsKey(start) || !g.containsKey(target)) {
            System.out.println(0);
            return;
        }

        // Corridor pruning: nodes reachable from start AND that can reach target
        Set<String> fromStart = reachable(g, start);
        Map<String, List<String>> rev = reverse(g);
        Set<String> toTarget = reachable(rev, target);

        Set<String> corridor = new HashSet<>(fromStart);
        corridor.retainAll(toTarget);

        // If target not reachable at all
        if (!corridor.contains(start) || !corridor.contains(target)) {
            System.out.println(0);
            return;
        }

        // If required nodes cannot appear on any start->...->out path, answer is 0
        if (!corridor.contains(req1) || !corridor.contains(req2)) {
            System.out.println(0);
            return;
        }

        // Infinite check on corridor
        if (hasCycleInSubgraph(g, corridor)) {
            System.out.println("INF");
            return;
        }

        // DP with mask: bit0 = visited dac, bit1 = visited fft
        Map<State, BigInteger> memo = new HashMap<>();
        int startMask = 0;
        if (start.equals(req1)) startMask |= 1;
        if (start.equals(req2)) startMask |= 2;

        BigInteger ans = count(g, corridor, start, target, req1, req2, startMask, memo);
        System.out.println(ans);
    }

    // DP: count paths from u to target, given visited-mask so far
    private static BigInteger count(
            Map<String, List<String>> g,
            Set<String> corridor,
            String u,
            String target,
            String req1,
            String req2,
            int mask,
            Map<State, BigInteger> memo
    ) {
        if (!corridor.contains(u)) return BigInteger.ZERO;

        // If we reached out: valid only if both required visited
        if (u.equals(target)) {
            return (mask == 3) ? BigInteger.ONE : BigInteger.ZERO;
        }

        State key = new State(u, mask);
        BigInteger cached = memo.get(key);
        if (cached != null) return cached;

        BigInteger sum = BigInteger.ZERO;
        for (String v : g.getOrDefault(u, List.of())) {
            if (!corridor.contains(v)) continue;

            int m2 = mask;
            if (v.equals(req1)) m2 |= 1;
            if (v.equals(req2)) m2 |= 2;

            sum = sum.add(count(g, corridor, v, target, req1, req2, m2, memo));
        }

        memo.put(key, sum);
        return sum;
    }

    private static final class State {
        final String node;
        final int mask;
        State(String node, int mask) { this.node = node; this.mask = mask; }
        @Override public boolean equals(Object o) {
            if (!(o instanceof State)) return false;
            State s = (State) o;
            return mask == s.mask && node.equals(s.node);
        }
        @Override public int hashCode() {
            return node.hashCode() * 31 + mask;
        }
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

    // Cycle detection in induced subgraph over "corridor"
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
