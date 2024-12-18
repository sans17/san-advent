package us.ligusan.advent.advent2024.d17;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class D17p2 {
    public static void main(final String[] args) throws Exception {
        var a = 0L;
        var b = 0L;
        var c = 0L;

        List<Integer> program = null;

        try (var scanner = new Scanner(D17p2.class.getResourceAsStream("input1.txt"))) {
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                System.out.println(s);
                var m = Pattern.compile("Register A: (\\d+)").matcher(s);
                m.find();
                a = Long.parseLong(m.group(1));

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Register B: (\\d+)").matcher(s);
                m.find();
                b = Long.parseLong(m.group(1));

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Register C: (\\d+)").matcher(s);
                m.find();
                c = Long.parseLong(m.group(1));

                s = scanner.nextLine();

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Program: ([,\\d]+)").matcher(s);
                m.find();
                program = Stream.of(m.group(1).split(",")).map(Integer::parseInt).toList();
            }
        }
        System.out.format("a=%d, b=%d, c=%d, program=%s%n", a, b, c, program);

        var digits = new ArrayList<>(Collections.nCopies(16, 0));
        for(var currentDigit = 0; currentDigit < 16; currentDigit++) {
            var base = digits.stream().mapToLong(d -> d).reduce(0L, (l, r) -> l * 8 + r);

            boolean found = false;
            for(int j = digits.get(currentDigit); j < 8; j++) {
                a = base + j * pow(8, 15 - currentDigit);
                System.out.format("currentDigit=%d, j=%d, a=%s%n", currentDigit, j, Long.toOctalString(a));

                var output = new ArrayList<Integer>();
                for(int p = 0, opcode = -1; p < program.size(); ) {
                    opcode = program.get(p);
                    var operand = program.get(p + 1);

                    var combo = 0L;
                    if(List.of(0, 2, 5, 6, 7).contains(opcode)) combo = switch(operand) {
                        case 0, 1, 2, 3 -> operand;
                        case 4 -> a;
                        case 5 -> b;
                        case 6 -> c;
                        default -> throw new IllegalArgumentException("Invalid value: " + operand);
                    };
                    var pow = 1L;
                    if(List.of(0, 6, 7).contains(opcode)) pow = pow(2, combo);

                    switch(opcode) {
                        case 0:
                            a /= pow;
                            break;
                        case 1:
                            b ^= operand;
                            break;
                        case 2:
                            b = combo % 8;
                            break;
                        case 3:
                            p = a == 0 ? p + 2 : operand;
                            continue;
                        case 4:
                            b ^= c;
                            break;
                        case 5:
                            output.add((int)(combo % 8));
                            break;
                        case 6:
                            b = a / pow;
                            break;
                        case 7:
                            c = a / pow;
                            break;
                    }

                    p += 2;
                }
                System.out.format("output=%s%n", output);

                var pos = 16 - currentDigit - 1;
                if(found = output.size() >= pos && output.get(pos).equals(program.get(pos))) {
                    digits.set(currentDigit, j);
                    break;
                }
            }
            if(!found) {
                currentDigit--;
                while(digits.get(currentDigit) == 7) {
                    digits.set(currentDigit, 0);
                    currentDigit--;
                }
                digits.set(currentDigit, digits.get(currentDigit) + 1);
                currentDigit--;
            }
        }
        System.out.println(digits);
    }

    private static long pow(long base, long exp) {
        var ret = 1L;
        for (long i = 0; i < exp; i++) ret *= base;
        return ret;
    }
}
