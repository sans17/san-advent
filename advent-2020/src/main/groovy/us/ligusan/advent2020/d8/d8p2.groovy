package us.ligusan.advent2020.d8;

List<Tuple2> list = new File(getClass().getResource('input.txt').toURI()).collect {
    matcher = it =~ /(nop|acc|jmp) ([+-]\d+)/

    [matcher[0][1], Integer.valueOf(matcher[0][2])]
}

HashSet<Integer> executed
int acc

int modified = -1
Queue<Integer> toModify

outer: for(;;) {
    executed = []
    acc = 0

    for(int i = 0; !executed.contains(i);) {
        Tuple2 instruction = i == modified ? ['nop' == list[i][0] ? 'jmp' : 'nop', list[i][1]] : list[i]

        //        println "${i} ${instruction}"

        executed << i

        int di = 1
        int da = 0

        switch(instruction[0]) {
            case 'acc':
                da = instruction[1]
                break
            case 'jmp':
                di = instruction[1]
        }

        acc += da
        i += di

        if(i == list.size()) break outer
    }

    if(modified < 0) toModify = executed as Queue
    while(list[modified = toModify.poll()][0] == 'acc');

        println "0: ${modified} ${list[modified]}"
}

println acc