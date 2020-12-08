package us.ligusan.advent2020.d8;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

HashMap<Integer, Tuple2> instructions = [:]

HashSet<Integer> executed
int acc

int modified = -1
Queue<Integer> toModify

outer: for(;;) {
    executed = []
    acc = 0

    for(int i = 0; !executed.contains(i);) {
        Tuple2 instruction = instructions[i]

        if(instruction == null) {
            matcher = list[i] =~ /(nop|acc|jmp) ([+-])(\d+)/

            instructions[i] = (instruction = new Tuple2(matcher[0][1], Integer.valueOf(matcher[0][2] + matcher[0][3])))
        }
        if(i == modified) instruction = new Tuple2('nop' == instruction[0] ? 'jmp' : 'nop', instruction[1])

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
    while(instructions[modified = toModify.poll()][0] == 'acc');

    println "0: ${modified} ${instructions[modified]}"
}

println acc