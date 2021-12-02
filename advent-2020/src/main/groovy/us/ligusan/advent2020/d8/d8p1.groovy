package us.ligusan.advent2020.d8;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

HashSet<Integer> executed = []

int acc = 0

for(int i = 0; !executed.contains(i);) {
    println "${i} ${list[i]}"

    executed << i

    matcher = list[i] =~ /(nop|acc|jmp) ([+-]\d+)/

    int di = 1
    int da = 0
    
    if(!'nop'.equals(matcher[0][1])) {
        int add = Integer.valueOf(matcher[0][2])

        if('acc'.equals(matcher[0][1])) da = add
        else di = add
    }
    
    acc += da
    i += di
}

println acc