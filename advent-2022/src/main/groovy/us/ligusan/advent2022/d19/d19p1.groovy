package us.ligusan.advent2022.d19

lines = new File(getClass().getResource('input.txt').toURI()).collect { it }

rs = lines.collect { 
    reg = (it =~ /Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian./)[0]
    println "reg=$reg"
    regi = reg[2..7].collect { Integer.valueOf(it) }
    [[regi[0], 0, 0, 0], [regi[1], 0, 0, 0], [regi[2], regi[3], 0, 0], [regi[4], 0, regi[5], 0]]
}
//println "rs=$rs"

cube = (1..4).collect { 0..1 }.combinations().findAll { it.sum() <= 1 }
println "cube=$cube"

def newRes(cres, rule, i) {
    [cres, rule[i]].transpose().collect { it[0] - it[1] }
}

count = rs.withIndex().collect { rule, i ->
    println "i=$i, rule=$rule"
    
    sps = [] as Set
    cps = [[(1..4).collect { 0 }, [1, 0, 0, 0]]]
    
    for(j=1; j<=22; j++) {
        nps = [] as Set
        
        cps.each { c ->
            cres = c[0]
            cro = c[1]
//            println "cres=$cres, cro=$cro"
            
            cube.each {
                ind = it.findIndexOf { it == 1 }
//                println "it=$it, ind=$ind"
                nres = ind < 0 ? cres : newRes(cres, rule, ind)
//                println "nres=$nres"
                if(nres.every { it >= 0 }) {
                    np = [[nres, cro].transpose()*.sum(), [cro, it].transpose()*.sum()]
//                    println "np=$np"
                    if(!sps.contains(np)) nps << np
                }
            }
        }
        
        sps += cps
        cps = nps
        
        println "j=$j, sps.size=${sps.size()}, cps.size=${cps.size()}"
    }
    
    ql = cps.collect { it[0][3] + 2 * it[1][3] + (newRes(it[0], rule, 3).every { it >= 0 } ? 1 : 0) }.max()
    println "ql=$ql"
    
    (i+1) * ql
}.sum()

println "count=$count"

