package us.ligusan.advent2022.d23

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

es = list.withIndex().collectMany { line, x ->
    Arrays.asList(line.toCharArray()).withIndex().findAll { it[0] == '#' }.collect { [x, it[1]] }
}
println "es=$es"

cube = (1..2).collect { -1..1 }.combinations()
println "cube=$cube"

ds = [
    [cube.findAll { it[0] == -1 }, [-1, 0]],
    [cube.findAll { it[0] == 1 }, [1, 0]],
    [cube.findAll { it[1] == -1 }, [0, -1]],
    [cube.findAll { it[1] == 1 }, [0, 1]]
    ]
println "ds=$ds"

for(i=1;; i++) {
    println "i=$i"
    ps = es.collect { e ->
        if(cube.findAll { it.any { it != 0 } }.collect { [e, it].transpose()*.sum() }.findAll { es.contains(it) }.empty) return null
        d = ds.find { it[0].every { !es.contains([e, it].transpose()*.sum()) } }
        d == null ? null : [e, d[1]].transpose()*.sum()
    }
    
//    println "ps=$ps"
    nes = ps.withIndex().collect { np, ind -> np == null || ps.findAll { it == np }.size() > 1 ? es[ind] : np }
    
    if(nes == es) break
    
    es = nes
//    println "es=$es"

//    xs = es.collect { it[0] }
//    ys = es.collect { it[1] }
//    println "xs=$xs, ys=$ys"
    
//    (xs.min()..xs.max()).each { x ->
//        (ys.min()..ys.max()).each { y -> print (es.contains([x, y]) ? '#' : '.') }
//        println ''
//    }
        
    ds << ds.pop()
//    println "ds=$ds"
}

//xs = es.collect { it[0] }
//ys = es.collect { it[1] }
//
//count = (xs.max() - xs.min() + 1) * (ys.max() - ys.min() + 1) - es.size()
//println "count=$count"
