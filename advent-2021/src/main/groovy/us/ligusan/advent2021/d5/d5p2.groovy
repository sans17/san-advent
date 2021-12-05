package us.ligusan.advent2021.d5

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def m = [:]
list.each { line ->
    def split = (line =~ /(\d+),(\d+) -> (\d+),(\d+)/)[0]
//    println split

    def x = split[1..-1].collect { Integer.valueOf(it) }
//    println x

    int xDiff = x[2] - x[0]
    int yDiff = x[3] - x[1]
    if(xDiff == 0 || yDiff == 0 || Math.abs(xDiff) == Math.abs(yDiff) ) {
        (0..Math.max(Math.abs(xDiff), Math.abs(yDiff))).each {
            def coord = [
                x[0] + it * Math.signum(xDiff),
                x[1] + it * Math.signum(yDiff)
            ]
//            println coord
            Integer h = m[coord]
            m[coord] = h == null ? 1 : h+1
        }
    }
}

//println m
//println (m.findAll { it.value > 1 })
println ((m.findAll { it.value > 1 }).size())