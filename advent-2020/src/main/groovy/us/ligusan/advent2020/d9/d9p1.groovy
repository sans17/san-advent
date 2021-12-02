package us.ligusan.advent2020.d9;

List<Long> list = new File(getClass().getResource('input.txt').toURI()).collect { Long.parseLong(it) }

for(int i = 0; i<list.size() - 25; i++) {
    long next = list[i + 25]

    //    println "${i + 25} ${next}"

    boolean sumFound = false
    second: for(int j = i; j < i+24; j++) for(int k = j+1; k < i + 25; k++) if(sumFound = (list[j] + list[k] == next)) break second

    if(!sumFound) {
        println next

        return
    }
}