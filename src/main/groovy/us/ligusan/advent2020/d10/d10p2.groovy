package us.ligusan.advent2020.d10;

List<Integer> list = new File(getClass().getResource('input.txt').toURI()).collect { Integer.parseInt(it) }

list = list.sort()
list.add(0, 0)
list << list[-1] + 3

println list

fib3list = [1, 2, 4]
int fib3(int n) {
    int size = fib3list.size()
    if(size < n-1) for(int i = size ; i < n-1 ; i++) fib3list << fib3list[i-3..i-1].sum()

    println "${n}: ${fib3list[n-2]}"
    return fib3list[n-2]
}

long ways = 1

int diff1 = 1

for(int i = 1; i<list.size(); i++)
    switch(list[i] - list[i-1]) {
        case 1:
            diff1++
            break
        case 1:
            println "no!!!"
            break
        case 3:
            if(diff1 > 1) ways *= fib3(diff1)
            println "${list[i]} ${diff1} ${ways}"
            diff1 = 1
    }


println ways