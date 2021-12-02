package us.ligusan.advent2020.d15;

int call = 1
HashMap<Integer, Integer> calls = []

[14, 8, 16, 0, 1, 17].each {
    calls[it] = call++
}
println calls

int next = 0
for(; call <= 2020; call++) {
    Integer last = calls[next]
    calls[next] = call

    println "${call} ${next} ${last}"

    next = last == null ? 0 : call - last
}
