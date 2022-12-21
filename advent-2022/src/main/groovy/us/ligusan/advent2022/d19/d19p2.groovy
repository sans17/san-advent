package us.ligusan.advent2022.d19

lines = new File(getClass().getResource('input.txt').toURI()).collect { it }

rs = lines.collect { 
    reg = (it =~ /Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian./)[0]
    println "reg=$reg"
    regi = reg[2..7].collect { Integer.valueOf(it) }
    [[regi[0], 0, 0, 0], [regi[1], 0, 0, 0], [regi[2], regi[3], 0, 0], [regi[4], 0, regi[5], 0]]
}
println "rs=$rs"

max_nt = 32

mm=1
for(i in 0..2) {
    rule = rs[i]
    println "rule=$rule"
    
    max = 0

    ps = [] as Set

    pps = [[[(1..4).collect { 0 }, [1, 0, 0, 0]]]]

    for(;!pps.empty;) {
        cres_cro = pps.pop()
        //    println "a: cres_cro=$cres_cro"

        nt = max_nt + 1 - cres_cro.size()
        cres = cres_cro[-1][0]
        cro = cres_cro[-1][1]

        mp = cres[3] + cro[3] * nt + (nt -1)*nt/2
        //    println "nt=$nt, mp=$mp, cres=$cres, cro=$cro, pps.size=${pps.size()}"
        if (mp <= max) continue

        ret = 0
        if(nt == 0) ret = cres[3]
        else if([cro, rule[3].collect { -it }].transpose()*.sum().every { it >= 0}) ret = mp

        if(ret > max) {
            max = ret
            println "nt=$nt, ps.size=${ps.size()}, pps.size=${pps.size()}, max=$max, cres_cro=$cres_cro"

            continue
        }

        if(!ps.contains([cres, cro])) {
            ps << [cres, cro]

            for(ind in [3, 2, 0, 1, -1]) {
                if(ind < 3 && ind >= 0 && cro[ind] > rule.collect { it[ind] }.max()) continue
                
                nres = ind < 0 ? cres : [cres, rule[ind].collect { -it } ].transpose()*.sum()
                //                println "ind=$ind, cro=$cro, nres=$nres"
                if(nres.any { it < 0 }) continue

                    ncrescro = cres_cro.clone()
                //                println "b: ncrescro=$ncrescro"
                ncro = [cro, (0..3).collect { it == ind ? 1 : 0 }].transpose()*.sum()
                ncrescro << [
                    [nres, cro].transpose()*.sum(),
                    ncro
                ]

                if(ncro[3]>0) pps.add(0, ncrescro)
                else pps << ncrescro
                //                println "b: ncrescro=$ncrescro"
            }
        }

        //    println "ps.size=${ps.size()}"
    }
    
    println "max=$max"
    
    mm *=max
}

println "mm=$mm"


