package us.ligusan.advent2020.d15;

int call = 1
HashMap<Integer, Integer> calls = []

[14, 8, 16, 0, 1, 17].each {
    calls[it] = call++
}
//println calls

Integer last = null
int next = 0
for(; call <= 30_000_000; call++) {
    next = last == null ? 0 : call-1 - last
    last = calls[next]

    calls[next] = call

    //        println "${call} ${next} ${last} ${calls}"
}

println next
