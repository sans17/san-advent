package us.ligusan.advent.advent2025.d10;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D10p2chat {

    // ===================== DEBUG / PROGRESS =====================
    static final boolean DEBUG = true;
    static final boolean TRACE_BRANCH = false;
    static final long REPORT_EVERY_NODES = 200_000;
    // ============================================================

    private static final Pattern ACTION = Pattern.compile("\\(([^)]*)\\)");
    private static final Pattern TARGET = Pattern.compile("\\{([^}]*)\\}");

    public static void main(String[] args) throws Exception {
        InputStream is = D10p2chat.class.getResourceAsStream("input.txt");
        if (is == null) is = System.in;

        long sum = 0;
        int lineNo = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                lineNo++;
                Instance inst = parseLine(line, lineNo);

                long t0 = System.currentTimeMillis();
                Solver solver = new Solver(inst);
                int ans = solver.solve();
                long ms = System.currentTimeMillis() - t0;

                if (ans < 0) throw new IllegalStateException("Unreachable target on line " + lineNo);

                sum += ans;

                if (DEBUG) {
                    System.out.println("--------------------------------------------------");
                    System.out.println("Line " + lineNo
                            + " | nCounters=" + inst.n
                            + " | nActions=" + inst.m
                            + " | answer=" + ans
                            + " | nodes=" + solver.nodesVisited
                            + " | time=" + ms + " ms");
                }
            }
        }

        System.out.println(sum);
    }

    // ---------------- Parsing ----------------

    static class Instance {
        final int n;           // counters
        final int m;           // actions
        final int[] target;    // size n
        final int[][] act;     // act[j] = list of counters incremented by action j (unique, sorted)
        final int[][] actionsByCounter; // for each counter i, list of actions that touch it

        Instance(int[] target, int[][] act) {
            this.n = target.length;
            this.m = act.length;
            this.target = target;
            this.act = act;

            List<List<Integer>> by = new ArrayList<>();
            for (int i = 0; i < n; i++) by.add(new ArrayList<>());

            for (int j = 0; j < m; j++) {
                for (int i : act[j]) by.get(i).add(j);
            }

            actionsByCounter = new int[n][];
            for (int i = 0; i < n; i++) {
                List<Integer> lst = by.get(i);
                actionsByCounter[i] = lst.stream().mapToInt(Integer::intValue).toArray();
            }
        }
    }

    static Instance parseLine(String line, int lineNo) {
        Matcher tm = TARGET.matcher(line);
        if (!tm.find()) throw new IllegalArgumentException("No {target} found on line " + lineNo);

        int[] target = parseIntList(tm.group(1).trim());
        if (target.length == 0) throw new IllegalArgumentException("Empty target on line " + lineNo);
        for (int v : target) if (v < 0) throw new IllegalArgumentException("Negative target on line " + lineNo);

        int n = target.length;

        List<int[]> actions = new ArrayList<>();
        Matcher am = ACTION.matcher(line);
        while (am.find()) {
            String inside = am.group(1).trim();
            if (inside.isEmpty()) continue;
            int[] idxs = parseIntList(inside);
            for (int idx : idxs) {
                if (idx < 0 || idx >= n) {
                    throw new IllegalArgumentException("Action index out of range on line " + lineNo + ": " + idx);
                }
            }
            idxs = dedupSorted(idxs);
            if (idxs.length > 0) actions.add(idxs);
        }

        return new Instance(target, actions.toArray(new int[0][]));
    }

    static int[] parseIntList(String s) {
        if (s.isEmpty()) return new int[0];
        String[] parts = s.split(",");
        int[] out = new int[parts.length];
        int k = 0;
        for (String p : parts) {
            p = p.trim();
            if (p.isEmpty()) continue;
            out[k++] = Integer.parseInt(p);
        }
        return k == out.length ? out : Arrays.copyOf(out, k);
    }

    static int[] dedupSorted(int[] a) {
        if (a.length <= 1) return a;
        Arrays.sort(a);
        int k = 1;
        for (int i = 1; i < a.length; i++) if (a[i] != a[i - 1]) a[k++] = a[i];
        return Arrays.copyOf(a, k);
    }

    // ---------------- Solver ----------------

    static class Solver {
        final Instance inst;
        long nodesVisited = 0;
        long nextReport = REPORT_EVERY_NODES;

        int best = Integer.MAX_VALUE;

        Solver(Instance inst) {
            this.inst = inst;
        }

        int solve() {
            // Quick infeasibility: any counter with target>0 but no action touches it
            for (int i = 0; i < inst.n; i++) {
                if (inst.target[i] > 0 && inst.actionsByCounter[i].length == 0) return -1;
            }

            int[] rem = inst.target.clone();     // remaining required
            int[] x = new int[inst.m];           // chosen counts (for fixed actions)
            boolean[] fixed = new boolean[inst.m];

            dfs(rem, x, fixed, 0);

            return best == Integer.MAX_VALUE ? -1 : best;
        }

        void dfs(int[] rem, int[] x, boolean[] fixed, int used) {
            nodesVisited++;
            if (DEBUG && nodesVisited >= nextReport) {
                nextReport += REPORT_EVERY_NODES;
                System.out.println("  nodes=" + nodesVisited + " best=" + (best == Integer.MAX_VALUE ? "INF" : best));
            }

            if (used >= best) return;

            for (int v : rem) if (v < 0) return;

            // Lower bound: need at least max(rem[i]) more actions
            int lb = 0;
            for (int v : rem) lb = Math.max(lb, v);
            if (used + lb >= best) return;

            // Propagate forced actions and FIX used-count bug by accumulating forcedAdds
            Deque<Change> changes = new ArrayDeque<>();
            int forcedAdds = propagate(rem, x, fixed, changes);
            if (forcedAdds < 0) {
                undo(rem, x, fixed, changes);
                return;
            }
            used += forcedAdds;

            if (used >= best) {
                undo(rem, x, fixed, changes);
                return;
            }

            // Check if done
            boolean done = true;
            for (int v : rem) { if (v != 0) { done = false; break; } }
            if (done) {
                if (used < best) {
                    best = used;
                    if (DEBUG) System.out.println("  New best = " + best);
                }
                undo(rem, x, fixed, changes);
                return;
            }

            // Choose branching variable: unfixed action with smallest upper bound
            int var = -1;
            int bestUB = Integer.MAX_VALUE;
            for (int j = 0; j < inst.m; j++) {
                if (fixed[j]) continue;
                int ub = upperBoundForAction(rem, j);
                if (ub < bestUB) { bestUB = ub; var = j; }
            }
            if (var == -1) {
                undo(rem, x, fixed, changes);
                return;
            }

            int j = var;
            int ub = upperBoundForAction(rem, j);

            // Try larger first
            for (int val = ub; val >= 0; val--) {
                if (used + val >= best) continue;

                if (TRACE_BRANCH) {
                    System.out.println("  branch action " + j + " = " + val + " (ub=" + ub + "), used=" + used);
                }

                Deque<Change> local = new ArrayDeque<>();
                applyActionCount(rem, x, fixed, j, val, local);

                dfs(rem, x, fixed, used + val);

                undo(rem, x, fixed, local);
            }

            undo(rem, x, fixed, changes);
        }

        // Upper bound for x_j: can't exceed any remaining counter it touches
        int upperBoundForAction(int[] rem, int j) {
            int ub = Integer.MAX_VALUE;
            for (int idx : inst.act[j]) ub = Math.min(ub, rem[idx]);
            return (ub == Integer.MAX_VALUE) ? 0 : ub;
        }

        /**
         * Propagation:
         * If counter i has rem[i] > 0 and among UNFIXED actions touching it there is exactly one action j,
         * then x_j is forced to rem[i].
         *
         * Returns total forced action-count added to objective, or -1 if infeasible.
         */
        int propagate(int[] rem, int[] x, boolean[] fixed, Deque<Change> changes) {
            int added = 0;

            boolean changed;
            do {
                changed = false;

                for (int i = 0; i < inst.n; i++) {
                    if (rem[i] == 0) continue;

                    int only = -1;
                    int cnt = 0;
                    for (int a : inst.actionsByCounter[i]) {
                        if (!fixed[a]) {
                            cnt++;
                            if (cnt == 1) only = a;
                            else break;
                        }
                    }

                    if (cnt == 0) return -1;

                    if (cnt == 1) {
                        int j = only;
                        int forced = rem[i];

                        // forced must not exceed other counters in that action
                        for (int idx : inst.act[j]) {
                            if (rem[idx] < forced) return -1;
                        }

                        applyActionCount(rem, x, fixed, j, forced, changes);
                        added += forced;
                        changed = true;
                        break; // restart scan after change
                    }
                }

                for (int v : rem) if (v < 0) return -1;

            } while (changed);

            return added;
        }

        void applyActionCount(int[] rem, int[] x, boolean[] fixed, int action, int count, Deque<Change> changes) {
            changes.push(Change.fixed(action, fixed[action]));
            fixed[action] = true;

            changes.push(Change.x(action, x[action]));
            x[action] = count;

            for (int idx : inst.act[action]) {
                changes.push(Change.rem(idx, rem[idx]));
                rem[idx] -= count;
            }
        }

        void undo(int[] rem, int[] x, boolean[] fixed, Deque<Change> changes) {
            while (!changes.isEmpty()) {
                Change c = changes.pop();
                switch (c.kind) {
                    case REM -> rem[c.idx] = c.prev;
                    case X   -> x[c.idx] = c.prev;
                    case FIX -> fixed[c.idx] = (c.prev != 0);
                }
            }
        }

        static class Change {
            enum Kind { REM, X, FIX }
            final Kind kind;
            final int idx;
            final int prev;
            Change(Kind kind, int idx, int prev) { this.kind = kind; this.idx = idx; this.prev = prev; }
            static Change rem(int idx, int prev) { return new Change(Kind.REM, idx, prev); }
            static Change x(int idx, int prev)   { return new Change(Kind.X, idx, prev); }
            static Change fixed(int idx, boolean prev) { return new Change(Kind.FIX, idx, prev ? 1 : 0); }
        }
    }
}
