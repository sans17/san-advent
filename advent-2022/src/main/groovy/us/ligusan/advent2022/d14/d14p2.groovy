package us.ligusan.advent2022.d14

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

rs = [] as Set
maxy = 0

list.each {
    lc=null
    it.split(' -> ').each {
        c=it.split(',').collect { Integer.valueOf(it) }
        if(lc != null) rs += lc[0]==c[0] ? (lc[1]..c[1]).collect { [lc[0], it] } : (lc[0]..c[0]).collect { [it, lc[1]] }
        lc = c
        if(maxy<c[1]) maxy=c[1]
    }
}

println rs
println maxy

counter=0
for(;;counter++) {
    println "counter=$counter"
    
    rc=[500,0]
    if(rs.contains(rc)) break
    
    stop: for(;;) {
        for(xc in [0, -1, 1]) {
            nrc = [rc, [xc, 1]].transpose()*.sum()
            println "rc=$rc"
            
            if(!rs.contains(nrc)) {
                if(nrc[1]>maxy+1) break
                rc=nrc
                continue stop
            }
        }
        
        println "rc=$rc"
        
        rs << rc
        break
    }
}

println counter
