package us.ligusan.advent2022.d20

list0 = new File(getClass().getResource('input.txt').toURI()).collect { new BigInteger(it) }
println list0

list = list0.collect { 811589153 * it }
println list


size=list.size()
println "size=$size"

lmap = list.withIndex().collect { v, i -> [i, v] }

(1..10).each {
    println "it=$it"
    for(i=0; i<size; i++) {
        e = lmap[i]
        
        lmap.each { if(it[0] > e[0]) it[0]-- }
    
//        println "e=$e"
        nind = e.sum().remainder(size -1)
        if(nind<0) nind+= (size -1)
//        println "nind=$nind"
        
        lmap.each { if(it[0] >= nind) it[0]++ }
        e[0] = nind
    }
}

ind0 = lmap.find { it[1] == BigInteger.ZERO }[0]
println "ind0=$ind0"
a = (1..3).collect {
    ind = it * 1000+ind0
    println "ind=$ind" 
    lmap.find { it[0] == ind%size }[1]
}
println "a=$a"
println a.sum()
