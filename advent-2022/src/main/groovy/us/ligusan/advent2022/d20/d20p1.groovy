package us.ligusan.advent2022.d20

list = new File(getClass().getResource('input.txt').toURI()).collect { new BigInteger(it) }

println list

size=list.size()
println "size=$size"
nlist = list.clone()

lmap = list.withIndex().collect { v, i -> [i, v] }

for(;!lmap.empty;) {
    e = lmap.pop()
    
    nlist.remove(e[0])
    lmap.each { if(it[0] > e[0]) it[0]-- }

    nind = e.sum()%(size -1)
    nlist.add(nind, e[1])
    println "e=$e, nind=$nind"
    
    lmap.each { if(it[0] >= nind) it[0]++ }
}

ind0 = nlist.indexOf(BigInteger.ZERO)
println "ind0=$ind0"
a = (1..3).collect {
    ind = it * 1000+ind0
    println "ind=$ind" 
    nlist[ind%size]
}
println "a=$a"
println a.sum()
