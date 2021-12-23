package us.ligusan.advent2021.d22

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def areas = []
list.each { line ->
	if(line.charAt(0) != '#') {
		def split = (line =~ /(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)/)[0]
		areas << [split[1] == 'on', split[2..-1].collect { Integer.valueOf(it) }]
	}
}
areas.each { println it }
println '---------------'

def area(r) {
    return (r.collate(2).collect { Math.abs(it[1] - it[0] + 1) }).inject(BigInteger.ONE) { m, n -> m*n }
}

def intersect(l, r) {
    def ret = null
    
    int x0 = Math.max(l[0], r[0])
    int x1 = Math.min(l[1], r[1])
    if(x0 <= x1) {
        int y0 = Math.max(l[2], r[2])
        int y1 = Math.min(l[3], r[3])
        if(y0 <= y1) {
            int z0 = Math.max(l[4], r[4])
            int z1 = Math.min(l[5], r[5])
            if(z0 <= z1) {
                ret = [x0, x1, y0, y1, z0, z1]
            }
        }
    }
//    println "l=${l}, r=${r}, ret=${ret}"
    
    return ret
}

def counter = BigInteger.ZERO
def sMap = [:]
areas.reverseEach { a ->
//    println '---------'
//    sMap.each { key, value ->
//        println "key=${key}, value=${value}"
//    }
//    println '---------'
    
    (sMap.keySet().sort().reverse()).each { key ->
        def value = sMap[key]
//        println "key=${key}, value=${value}"
        
        def newI = value.findResults { intersect(it, a[1]) }
//        println "newI=${newI}"
        if(!newI.empty) {
            def level = sMap[key+1]
            if(level == null) sMap[key+1] = (level = [])
            level.addAll(newI)
            if(a[0]) {
                def s = (newI.collect { area(it) }).sum()
                counter += (key%2 == 1 ? -1 : 1) * s
//                println "key=${key}, s=${s}, counter=${counter}"
            }
        }
    }
    def level = sMap[1]
    if(level == null) sMap[1] = (level = [])
    level << a[1]
    if(a[0]) {
        counter += area(a[1])
//        println "counter=${counter}"
    }
}
println counter