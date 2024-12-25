package us.ligusan.advent.advent2024.d25;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class D25p1 {
    public static void main(final String[] args) throws Exception {
//        var file = "testInput.txt";
//        var file = "testInput1.txt";
        var file = "input.txt";

        var locks = new ArrayList<List<Integer>>();
        var keys = new ArrayList<List<Integer>>();

        var keyLockFlag = 0;
        List<Integer> keyLock = null;
        try (var scanner = new Scanner(D25p1.class.getResourceAsStream(file))) {
            for (String s; scanner.hasNextLine(); ) {
                s = scanner.nextLine();
                System.out.println(s);

                if (s.isEmpty()) {
                    (keyLockFlag == 1 ? locks : keys).add(keyLock);
                    keyLockFlag = 0;
                } else if (keyLockFlag == 0) {
                    keyLockFlag = s.charAt(0) == '#' ? 1 : -1;
                    keyLock = new ArrayList<>(Collections.nCopies(s.length(), 0));
                } else for (var i = 0; i < s.length(); i++)
                    if (s.charAt(i) == '#')
                        keyLock.set(i, keyLock.get(i) + keyLockFlag);
            }
        }
        (keyLockFlag == 1 ? locks : keys).add(keyLock);
        System.out.format("locks=%s%n", locks);
        System.out.format("keys=%s%n", keys);

        var counter = 0;
        for (var l : locks)
            for (var k : keys) {
                counter++;
                for (var i = 0; i < l.size(); i++)
                    if (l.get(i) - k.get(i) >= 7) {
                        counter--;
                        break;
                    }
            }
        System.out.println(counter);
    }
}
