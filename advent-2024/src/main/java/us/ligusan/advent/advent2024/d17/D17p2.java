package us.ligusan.advent.advent2024.d17;

public class D17p2 {
    public static void main(final String[] args) throws Exception {
        long program = 02413750343155530L;

        var lower = 04257155L;

        var base = pow(8, 7);
        var top = 6 * pow(8, 15);
        var maxDigit = 14;
        System.out.format("top=%s, base=%s, lower=%s, %n", Long.toOctalString(top), Long.toOctalString(base), Long.toOctalString(lower));
        for (var i = 0L; i < pow(8, 8); i++) {
            var a = top + i * base + lower;
            if (i % 1000_000 == 0) System.out.format("i=%s, a=%s%n", Long.toOctalString(i), Long.toOctalString(a));

            var output = 0L;
            for (var k = 0; k < 16; k++) {
                var b = (a % 8) ^ 3;
                b ^= (a / pow(2, b));
                b ^= 5;
                a /= 8;
                output = output * 8 + b % 8;
            }

            var notEqual = false;
            var j = 15;
            for (; j >= 0; j--) if (notEqual = getDigit(output, j) != getDigit(program, j)) break;
            if (notEqual && j <= maxDigit) {
                maxDigit = j - 1;
                System.out.format("maxDigit=%d%n", maxDigit);
                System.out.format("i=%s, a=%s, output=%s, program=%s%n", Long.toOctalString(i), Long.toOctalString(a), Long.toOctalString(output), Long.toOctalString(program));
            }
            if (!notEqual) {
                System.out.format("a=%d, output=%s, program=%s%n", a, Long.toOctalString(output), Long.toOctalString(program));
                break;
            }
        }
    }

    private static long pow(long base, long exp) {
        var ret = 1L;
        for (long i = 0; i < exp; i++) ret *= base;
        return ret;
    }

    private static int getDigit(long number, int position) {
        return (int) (number / pow(8, position) % 8);
    }
}
