package us.ligusan.advent2021.d5

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def m = [:]
list.each { line ->
    def split = (line =~ /(\d+),(\d+) -> (\d+),(\d+)/)[0]
    println split
    
    def x = split[1..-1].collect { Integer.valueOf(it) }
    println x
    
    boolean v = x[0] == x[2]
    if(v || x[1] == x[3]) {
        (v ? x[1]..x[3] : x[0]..x[2]).each {
            println "${v ? x[0] : it} : ${v ? it : x[1]}"
            def coord = v ? [x[0], it] : [it, x[1]]
            Integer h = m[coord]
            m[coord] = h == null ? 1 : h+1
        }
    }
}

println m
println (m.findAll { it.value > 1 })
println ((m.findAll { it.value > 1 }).size())