package us.ligusan.advent2021.d19

def list = new File(getClass().getResource('example.txt').toURI()).collect { it }

def mult(list, v) {
    return list.collect { [it, v].transpose()*.inject(1) { m, n -> m*n } }
}
def move(list, v) {
    return list.collect { [it, v].transpose()*.sum() }
}

def sq(l, r) {
    def x = l[0] - r[0]
    def y = l[1] - r[1]
    def z = l[2] - r[2]
    return x*x + y*y + z*z
}
def sq(list) {
    ([list, list].combinations().collect { sq(it[0], it[1]) }).sum()/2
}
def sort(list) {
    list.sort {
        def c = list.collect { it }
        c.remove(it)
        sq(c)
    }
}

def getRs(list) {
    def indexes = ((1..4).collect { 0..(list.size()-1) }).combinations().findAll { it == it.sort() && it.size() == it.toSet().size() }
    return indexes.collectEntries {
        def values = it.collect { list[it] }
        [values, sq(values)]
    }
}

def rots = []
rots += [[[1, 0, 0], [0, 1, 0], [0, 0, 1]], [[1, 0, 0], [0, 0, -1], [0, 1, 0]], [[1, 0, 0], [0, -1, 1], [0, 0, -1]], [[1, 0, 0], [0, 0, 1], [0, -1, 0]]]
rots += [[[-1, 0, 0], [0, 1, 0], [0, 0, -1]], [[-1, 0, 0], [0, 0, 1], [0, 1, 0]], [[-1, 0, 0], [0, -1, 0], [0, 0, 1]], [[-1, 0, 0], [0, 0, -1], [0, 0, -1]]]
rots += [[[0, 1, 0], [1, 0, 0], [0, 0, -1]], [[0, 1, 0], [0, 0, 1], [1, 0, 0]], [[0, 1, 0], [-1, 0, 0], [0, 0, 1]], [[0, 1, 0], [0, 0, -1], [-1, 0, 0]]]
rots += [[[0, -1, 0], [1, 0, 0], [0, 0, 1]], [[0, -1, 0], [0, 0, 1], [-1, 0, 0]], [[0, -1, 0], [-1, 0, 0], [0, 0, -1]], [[0, -1, 0], [0, 0, -1], [1, 0, 0]]]
rots += [[[0, 0, 1], [1, 0, 0], [0, 1, 0]], [[0, 0, 1], [0, 1, 0], [-1, 0, 0]], [[0, 0, 1], [-1, 0, 0], [0, -1, 0]], [[0, 0, 1], [0, -1, 0], [1, 0, 0]]]
rots += [[[0, 0, -1], [1, 0, 0], [0, -1, 0]], [[0, 0, -1], [0, 1, 0], [1, 0, 0]], [[0, 0, -1], [-1, 0, 0], [0, 1, 0]], [[0, 0, -1], [0, -1, 0], [-1, 0, 0]]]

def matMult(point, mat) {
    return mat.collect { ([point, it].transpose()*.inject(1) { m, n -> m*n }).sum() }
}
        
def sMap = [:]

int s = -1
def dList = []
list.each { line ->
    if(line == '') {
        sMap[s] = dList
        //        println "s=${s}, dList=${dList}"

        s = -1
        dList = []
    }
    else if (s >= 0) {
        def split = (line =~ /(-?\d+),(-?\d+),(-?\d+)/)[0]
        dList << split[1..3].collect { Integer.valueOf(it) }
    } else {
        def split = (line =~ /--- scanner (\d+) ---/)[0]
        s = Integer.valueOf(split[1])
        //        println "s=${s}"
    }
}
sMap[s] = dList
//sMap.each { key, value ->
//    println "key=${key}, value=${value}"
//}

def r0 = getRs(sMap[0])
def r1 = getRs(sMap[1])
([r0, r1].combinations().findAll { it[0].value == it[1].value }).each { println "value=${it[0].value}, r0.key=${it[0].key}, r1.key=${it[1].key}" }

def a0 = sMap.values().collect { sort(it) }

def a1 = a0.collect { r -> move(r, r[0].collect { -it }) }
println rots.find { rot -> [a1[0], a1[1]].transpose().every { matMult(it[0], rot) == it[1] } }
