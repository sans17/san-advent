package us.ligusan.advent.advent2025.d9;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Largest axis-aligned rectangle (inclusive lattice area) fully inside/on an orthogonal polygon,
 * where the rectangle's opposite corners are chosen from the input points.
 *
 * Polygon can have holes and touching sides. We consider integer lattice points only.
 *
 * Input format: one or more rings separated by blank lines.
 * Each line: "x,y"
 * Each ring is an orthogonal closed walk; closure is auto-added if missing.
 *
 * Rectangle validity:
 * - Choose any two input points as opposite corners.
 * - Rectangle includes all integer lattice points (inclusive).
 * - Rectangle is valid iff every lattice point in it is inside OR on boundary of the polygon (holes excluded).
 *
 * Output: maximum inclusive area.
 */
public class D9p2chat {

    static class Pt {
        final int x, y;
        Pt(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            if (!(o instanceof Pt)) return false;
            Pt p = (Pt)o;
            return x == p.x && y == p.y;
        }
        @Override public int hashCode() { return Objects.hash(x, y); }
        @Override public String toString() { return x + "," + y; }
    }

    static class Edge {
        final int x1, y1, x2, y2;
        Edge(int x1, int y1, int x2, int y2) {
            this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
        }
        boolean isHorizontal() { return y1 == y2; }
        boolean isVertical() { return x1 == x2; }
    }

    public static void main(String[] args) throws Exception {
        List<List<Pt>> rings = readRingsFromResource("input.txt");
        if (rings.isEmpty()) { System.out.println(0); return; }

        // Unique input points (only these can be opposite corners)
        List<Pt> points;
        {
            Set<Pt> set = new HashSet<>();
            for (List<Pt> r : rings) for (Pt p : r) set.add(p);
            points = new ArrayList<>(set);
        }
        if (points.size() < 2) { System.out.println(0); return; }

        // Collect edges from all rings + overall bounds
        List<Edge> edges = new ArrayList<>();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (List<Pt> ring : rings) {
            for (Pt p : ring) {
                minX = Math.min(minX, p.x); maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y); maxY = Math.max(maxY, p.y);
            }
            for (int i = 0; i + 1 < ring.size(); i++) {
                Pt a = ring.get(i), b = ring.get(i + 1);
                if (!(a.x == b.x || a.y == b.y)) {
                    throw new IllegalArgumentException("Non-orthogonal edge: " + a + " -> " + b);
                }
                edges.add(new Edge(a.x, a.y, b.x, b.y));
            }
        }

        // Coordinate compression on integer lattice points:
        // We include vertex coords and coord+1 so polygon boundaries never cut through a block interior.
        int[] xBreaks = buildBreaks(points, true);
        int[] yBreaks = buildBreaks(points, false);
        xBreaks = ensureContains(xBreaks, minX, maxX);
        yBreaks = ensureContains(yBreaks, minY, maxY);

        int nx = xBreaks.length - 1;
        int ny = yBreaks.length - 1;
        if (nx <= 0 || ny <= 0) { System.out.println(0); return; }

        int[] xLen = new int[nx];
        int[] yLen = new int[ny];
        for (int i = 0; i < nx; i++) xLen[i] = xBreaks[i + 1] - xBreaks[i]; // # integer x in block
        for (int j = 0; j < ny; j++) yLen[j] = yBreaks[j + 1] - yBreaks[j]; // # integer y in block

        // Determine inside/on-boundary for each block by testing a representative lattice point:
        // (xBreaks[i], yBreaks[j]) is always an integer lattice point.
        // Because of our breaks construction, inside status is constant across the whole block.
        long[][] ps = new long[ny + 1][nx + 1];
        for (int j = 0; j < ny; j++) {
            long rowRun = 0;
            int y = yBreaks[j];
            for (int i = 0; i < nx; i++) {
                int x = xBreaks[i];
                boolean inside = pointInsideOrOnBoundaryEvenOdd(x, y, edges);
                long w = inside ? (long) xLen[i] * (long) yLen[j] : 0L;
                rowRun += w;
                ps[j + 1][i + 1] = ps[j][i + 1] + rowRun;
            }
        }

        long best = 0;

        // Enumerate all pairs of input points as opposite corners
        for (int a = 0; a < points.size(); a++) {
            Pt p = points.get(a);
            for (int b = a + 1; b < points.size(); b++) {
                Pt q = points.get(b);

                int x1 = Math.min(p.x, q.x), x2 = Math.max(p.x, q.x);
                int y1 = Math.min(p.y, q.y), y2 = Math.max(p.y, q.y);

                long area = (long)(x2 - x1 + 1) * (long)(y2 - y1 + 1);
                if (area <= best) continue;

                int ix1 = blockIndexOf(xBreaks, x1);
                int ix2 = blockIndexOf(xBreaks, x2);
                int iy1 = blockIndexOf(yBreaks, y1);
                int iy2 = blockIndexOf(yBreaks, y2);

                long insideCount = rectSum(ps, ix1, iy1, ix2, iy2);
                if (insideCount == area) best = area;
            }
        }

        System.out.println(best);
    }

    /**
     * Point-in-polygon with boundary included, using even-odd rule over ALL edges from ALL rings.
     * This correctly handles holes (as long as rings represent boundaries and don't "overlap" except touching).
     */
    static boolean pointInsideOrOnBoundaryEvenOdd(int x, int y, List<Edge> edges) {
        // 1) boundary check (orthogonal edges => easy)
        for (Edge e : edges) {
            if (pointOnSegmentOrthogonal(x, y, e)) return true;
        }

        // 2) even-odd ray casting to +infinity in x direction.
        // Standard robust rule: count crossings with edges that straddle y strictly (y1 > y) != (y2 > y)
        // and with intersection xInt > x.
        boolean inside = false;
        for (Edge e : edges) {
            int x1 = e.x1, y1 = e.y1, x2 = e.x2, y2 = e.y2;

            // horizontal edges don't cross a horizontal ray in a stable way; skip them for crossings
            if (y1 == y2) continue;

            // Does the edge straddle y?
            boolean above1 = y1 > y;
            boolean above2 = y2 > y;
            if (above1 == above2) continue;

            // Edge is vertical (orthogonal input), intersection x is constant = x1 (=x2)
            int xInt = x1;

            if (xInt > x) inside = !inside;
        }
        return inside;
    }

    static boolean pointOnSegmentOrthogonal(int x, int y, Edge e) {
        if (e.x1 == e.x2) { // vertical
            if (x != e.x1) return false;
            int lo = Math.min(e.y1, e.y2);
            int hi = Math.max(e.y1, e.y2);
            return y >= lo && y <= hi;
        } else { // horizontal
            if (y != e.y1) return false;
            int lo = Math.min(e.x1, e.x2);
            int hi = Math.max(e.x1, e.x2);
            return x >= lo && x <= hi;
        }
    }

    // Prefix sum query over inclusive block rectangle [ix1..ix2] x [iy1..iy2]
    static long rectSum(long[][] ps, int ix1, int iy1, int ix2, int iy2) {
        int x1 = ix1, x2 = ix2 + 1;
        int y1 = iy1, y2 = iy2 + 1;
        long A = ps[y1][x1];
        long B = ps[y1][x2];
        long C = ps[y2][x1];
        long D = ps[y2][x2];
        return D - B - C + A;
    }

    // Finds block index i such that breaks[i] <= coord < breaks[i+1]
    static int blockIndexOf(int[] breaks, int coord) {
        int pos = Arrays.binarySearch(breaks, coord);
        if (pos >= 0) return Math.min(pos, breaks.length - 2);
        int ins = -pos - 1;
        return ins - 1;
    }

    static int[] buildBreaks(List<Pt> pts, boolean isX) {
        int n = pts.size();
        int[] tmp = new int[n * 2];
        int k = 0;
        for (Pt p : pts) {
            int v = isX ? p.x : p.y;
            tmp[k++] = v;
            tmp[k++] = v + 1;
        }
        Arrays.sort(tmp, 0, k);
        int[] uniq = new int[k];
        int m = 0;
        for (int i = 0; i < k; i++) {
            if (m == 0 || tmp[i] != uniq[m - 1]) uniq[m++] = tmp[i];
        }
        return Arrays.copyOf(uniq, m);
    }

    static int[] ensureContains(int[] breaks, int min, int max) {
        int needA = min;
        int needB = max + 1;
        boolean hasA = Arrays.binarySearch(breaks, needA) >= 0;
        boolean hasB = Arrays.binarySearch(breaks, needB) >= 0;
        if (hasA && hasB) return breaks;

        int extra = (hasA ? 0 : 1) + (hasB ? 0 : 1);
        int[] out = Arrays.copyOf(breaks, breaks.length + extra);
        int idx = breaks.length;
        if (!hasA) out[idx++] = needA;
        if (!hasB) out[idx++] = needB;
        Arrays.sort(out, 0, idx);

        int[] uniq = new int[idx];
        int m = 0;
        for (int i = 0; i < idx; i++) {
            if (m == 0 || out[i] != uniq[m - 1]) uniq[m++] = out[i];
        }
        return Arrays.copyOf(uniq, m);
    }

    // Rings separated by blank lines.
    static List<List<Pt>> readRingsFromResource(String resourceName) throws IOException {
        InputStream is = D9p2chat.class.getResourceAsStream(resourceName);
        if (is == null) throw new FileNotFoundException("Resource not found on classpath: " + resourceName);

        List<List<Pt>> rings = new ArrayList<>();
        List<Pt> cur = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (!cur.isEmpty()) { rings.add(closeRing(cur)); cur = new ArrayList<>(); }
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length != 2) throw new IllegalArgumentException("Bad line: " + line);
                cur.add(new Pt(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())));
            }
        }
        if (!cur.isEmpty()) rings.add(closeRing(cur));
        rings.removeIf(r -> r.size() < 4);
        return rings;
    }

    static List<Pt> closeRing(List<Pt> ring) {
        Pt first = ring.get(0), last = ring.get(ring.size() - 1);
        if (first.x != last.x || first.y != last.y) ring.add(new Pt(first.x, first.y));
        return ring;
    }
}
