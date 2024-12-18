package us.ligusan.advent.advent2024.d17;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class D17p1 {
    public static void main(final String[] args) throws Exception {
        var a = 0;
        var b = 0;
        var c = 0;

        List<Integer> program = null;

        try (var scanner = new Scanner(D17p1.class.getResourceAsStream("input.txt"))) {
            while (scanner.hasNextLine()) {
                var s = scanner.nextLine();
                System.out.println(s);
                var m = Pattern.compile("Register A: (\\d+)").matcher(s);
                m.find();
                a = Integer.parseInt(m.group(1));

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Register B: (\\d+)").matcher(s);
                m.find();
                b = Integer.parseInt(m.group(1));

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Register C: (\\d+)").matcher(s);
                m.find();
                c = Integer.parseInt(m.group(1));

                s = scanner.nextLine();

                s = scanner.nextLine();
                System.out.println(s);
                m = Pattern.compile("Program: ([,\\d]+)").matcher(s);
                m.find();
                program = Stream.of(m.group(1).split(",")).map(Integer::parseInt).toList();
            }
        }
        System.out.format("a=%d, b=%d, c=%d, program=%s%n", a, b, c, program);

        var output = new StringBuilder();
        for(int p = 0, opcode = -1; p < program.size(); ) {
            if(opcode == 5) output.append(',');
            opcode = program.get(p);
            var operand = program.get(p + 1);

            int combo = 0;
            if (List.of(0, 2, 5, 6, 7).contains(opcode))
                combo = switch(operand) {
                    case 0, 1, 2, 3 -> operand;
                    case 4 -> a;
                    case 5 -> b;
                    case 6 -> c;
                    default -> throw new IllegalArgumentException("Invalid value: " + operand);
                };
            int pow = 1;
            if (List.of(0, 6, 7).contains(opcode))
                for(int i = 0; i < combo; i++) pow *= 2;

            switch(opcode) {
                case 0: a /= pow; break;
                case 1: b ^= operand; break;
                case 2: b = combo % 8; break;
                case 3: p = a == 0 ? p+2 : operand; continue;
                case 4: b ^= c; break;
                case 5: output.append(combo % 8); break;
                case 6: b = a / pow; break;
                case 7: c = a / pow; break;
            }

            System.out.format("p=%d, opcode=%d, operand=%d, combo=%d, pow=%d, a=%d, b=%d, c=%d, output=%s%n", p, opcode, operand, combo, pow, a, b, c, output);

            p += 2;
        }
    }
}
