package us.ligusan.advent2020.d10;

List<Integer> list = new File(getClass().getResource('input.txt').toURI()).collect { Integer.parseInt(it) }

list = list.sort()

println list

int diff1 = 1
int diff3 = 1

for(int i = 1; i<list.size(); i++)
    switch(list[i] - list[i-1]) {
        case 1:
            diff1++
            break
        case 3: diff3++
    }

println "${diff1} ${diff3} ${diff1 * diff3}"