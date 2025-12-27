package us.ligusan.advent.advent2025.d10;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For each input line:
 *   [state] (action1) (action2) ... {ignored}
 *
 * state: '.' = off, '#' = on (0-based index from left)
 * each action: list of indices to toggle (XOR)
 *
 * Starting from all-off, find the minimal number of actions needed to reach the given state.
 * Print the SUM of these minima over all lines.
 *
 * Notes:
 * - This uses BFS over all 2^n states, so it assumes n is small (typical for switch puzzles).
 * - Uses long bitmasks, supports up to 62 switches (but BFS only feasible for small n).
 */
public class D10p1chat {

    private static final Pattern STATE = Pattern.compile("\\[(.*?)]");
    private static final Pattern ACTION = Pattern.compile("\\(([^)]*)\\)");

    public static void main(String[] args) throws Exception {
        // Change file name if needed:
        InputStream is = D10p1chat.class.getResourceAsStream("input.txt");
        if (is == null) {
            // fallback to stdin if resource not found
            is = System.in;
        }

        long sum = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Parsed parsed = parseLine(line);
                int steps = minActionsBfs(parsed.n, parsed.targetMask, parsed.actionMasks);

                // If unreachable, you can decide what to do. Here we treat it as 0.
                // Alternatively: throw, or add -1, etc.
                if (steps < 0) {
                    throw new IllegalStateException("Unreachable state for line: " + line);
                }
                sum += steps;
            }
        }

        System.out.println(sum);
    }

    private static class Parsed {
        final int n;
        final long targetMask;
        final long[] actionMasks;
        Parsed(int n, long targetMask, long[] actionMasks) {
            this.n = n;
            this.targetMask = targetMask;
            this.actionMasks = actionMasks;
        }
    }

    private static Parsed parseLine(String line) {
        Matcher sm = STATE.matcher(line);
        if (!sm.find()) throw new IllegalArgumentException("No [state] found: " + line);
        String s = sm.group(1).trim();
        int n = s.length();
        if (n <= 0) throw new IllegalArgumentException("Empty state: " + line);
        if (n >= 63) throw new IllegalArgumentException("Too many switches for 64-bit mask: n=" + n);

        long target = 0L;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c == '#') target |= (1L << i);
            else if (c != '.') throw new IllegalArgumentException("Bad char in state: " + c + " in " + line);
        }

        List<Long> actions = new ArrayList<>();
        Matcher am = ACTION.matcher(line);
        while (am.find()) {
            String inside = am.group(1).trim();
            if (inside.isEmpty()) {
                // empty action toggles nothing; ignore
                continue;
            }
            long mask = 0L;
            String[] parts = inside.split(",");
            for (String p : parts) {
                p = p.trim();
                if (p.isEmpty()) continue;
                int idx = Integer.parseInt(p);
                if (idx < 0 || idx >= n) {
                    throw new IllegalArgumentException("Toggle index out of range: " + idx + " for n=" + n + " in " + line);
                }
                mask ^= (1L << idx); // XOR handles accidental duplicates
            }
            if (mask != 0L) actions.add(mask);
        }

        long[] actionMasks = new long[actions.size()];
        for (int i = 0; i < actions.size(); i++) actionMasks[i] = actions.get(i);

        return new Parsed(n, target, actionMasks);
    }

    /**
     * Unweighted shortest path in the graph of states (bitmasks),
     * edges: apply any action (XOR with action mask).
     */
    private static int minActionsBfs(int n, long target, long[] actions) {
        if (target == 0L) return 0;
        if (actions.length == 0) return -1;

        // BFS over 2^n states; only feasible for relatively small n.
        if (n > 25) {
            // Still might work if actions small, but generally explodes.
            // If your n is larger, you need a different approach (problem becomes hard in general).
            throw new IllegalArgumentException("n too large for BFS: " + n);
        }

        int size = 1 << n;
        int[] dist = new int[size];
        Arrays.fill(dist, -1);

        ArrayDeque<Integer> q = new ArrayDeque<>();
        dist[0] = 0;
        q.add(0);

        int targetInt = (int) target;

        while (!q.isEmpty()) {
            int cur = q.poll();
            int d = dist[cur];
            for (long aMaskL : actions) {
                int nxt = cur ^ (int) aMaskL;
                if (dist[nxt] != -1) continue;
                dist[nxt] = d + 1;
                if (nxt == targetInt) return dist[nxt];
                q.add(nxt);
            }
        }
        return -1;
    }
}
