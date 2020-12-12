package us.ligusan.advent2020.d12;

List<Tuple2<String, Integer>> instructions = new File(getClass().getResource('input.txt').toURI()).collect {
    matcher = it =~ /(N|S|E|W|L|R|F)(\d+)/

    [
        matcher[0][1],
        Integer.valueOf(matcher[0][2])
    ]
}

Tuple2<Integer, Integer> move(String direction, int length) {
    switch(direction) {
        case 'N': return [length, 0]
        case 'S': return [-length, 0]
        case 'E': return [0, length]
        case 'W': return [0, -length]
    }
}

int x = 0
int y = 0

int direction = 0
List<String> directions = ['E', 'N', 'W', 'S']

instructions.each {
    //    println " 0: ${it}"

    Tuple2<Integer, Integer> nextMove = [0, 0]
    switch(it[0]) {
        case 'N':
        case 'S':
        case 'E':
        case 'W':
            nextMove = move(it[0], it[1])
            break
        case 'L':
            direction = (direction + (int)(it[1]/90)) % 4
            break
        case 'R':
            direction = (direction - (int)(it[1]/90)) % 4
            break
        case 'F': nextMove = move(directions[direction], it[1])
    }

    x += nextMove[0]
    y += nextMove[1]

    //    println "10: ${nextMove} ${x} ${y} ${direction} ${directions[direction]}"
}

println "${x} ${y} ${Math.abs(x) + Math.abs(y)}"
