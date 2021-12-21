package us.ligusan.advent2021.d19

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

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

def getTs(list) {
	def indexes = ((1..4).collect { 0..(list.size()-1) }).combinations().findAll { it == it.sort() && it.size() == it.toSet().size() }
	return indexes.collectEntries {
		def values = it.collect { list[it] }
		[values, sq(values)]
	}
}

rots = []
rots += [[[1, 0, 0], [0, 1, 0], [0, 0, 1]], [[1, 0, 0], [0, 0, -1], [0, 1, 0]], [[1, 0, 0], [0, -1, 1], [0, 0, -1]], [[1, 0, 0], [0, 0, 1], [0, -1, 0]]]
rots += [[[-1, 0, 0], [0, 1, 0], [0, 0, -1]], [[-1, 0, 0], [0, 0, 1], [0, 1, 0]], [[-1, 0, 0], [0, -1, 0], [0, 0, 1]], [[-1, 0, 0], [0, 0, -1], [0, 0, -1]]]
rots += [[[0, 1, 0], [1, 0, 0], [0, 0, -1]], [[0, 1, 0], [0, 0, 1], [1, 0, 0]], [[0, 1, 0], [-1, 0, 0], [0, 0, 1]], [[0, 1, 0], [0, 0, -1], [-1, 0, 0]]]
rots += [[[0, -1, 0], [1, 0, 0], [0, 0, 1]], [[0, -1, 0], [0, 0, 1], [-1, 0, 0]], [[0, -1, 0], [-1, 0, 0], [0, 0, -1]], [[0, -1, 0], [0, 0, -1], [1, 0, 0]]]
rots += [[[0, 0, 1], [1, 0, 0], [0, 1, 0]], [[0, 0, 1], [0, 1, 0], [-1, 0, 0]], [[0, 0, 1], [-1, 0, 0], [0, -1, 0]], [[0, 0, 1], [0, -1, 0], [1, 0, 0]]]
rots += [[[0, 0, -1], [1, 0, 0], [0, -1, 0]], [[0, 0, -1], [0, 1, 0], [1, 0, 0]], [[0, 0, -1], [-1, 0, 0], [0, 1, 0]], [[0, 0, -1], [0, -1, 0], [-1, 0, 0]]]

def matMult(point, mat) {
	return mat.collect { ([point, it].transpose()*.inject(1) { m, n -> m*n }).sum() }
}

def findTrans(l, r) {
	def a0 = [l, r].collect { sort(it) }
	def m = a0.collect { t -> t[0].collect { -it } }
	def a1 = a0.withIndex().collect { t, index -> move(t, m[index]) }
	def rot = rots.find { rot -> [a1[0], a1[1]].transpose().every { matMult(it[0], rot) == it[1] } }
	
	return [rot, [matMult(m[0], rot), a0[1][0]].transpose()*.sum()]
}
def applyTrans(list, trans) {
	move(list.collect { matMult(it, trans[0]) }, trans[1])
}
		
def sMap = []

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

def tMap = sMap.collect { getTs(it) }

def transMap = [:]

def links = [[25, 12], [12, 20], [16, 1], [10, 7], [21, 24], [24, 7], [7, 6], [13, 18], [18, 14], [15, 19], [19, 23], [23, 14], [14, 5], [0, 6], [6, 11], [1, 17], [17, 3], [2, 9], [9, 11], [3, 4], [4, 11], [5, 8], [8, 22], [22, 11], [20, 11]]
println links

links.each {
	println it
	loop: for(def entry0 : tMap[it[0]]) for(def entry1 : tMap[it[1]]) {
		if(entry0.value == entry1.value) {
//			println "entry0=${entry0}, entry1=${entry1}"
			def t = findTrans(entry0.key, entry1.key)
			def newS = applyTrans(sMap[it[0]], t)
			newS.retainAll(sMap[it[1]])
			if(newS.size() >= 12) {
				println "t=${t}, newS.size()=${newS.size()}"
				transMap[it] = t
				break loop
			}
		}
	}
}
println '----------'
transMap.each { println it }

def wMap = sMap.withIndex().collectEntries { value, index -> [index, [[0, 0, 0]]] }
links.each {
	println "it=${it}, wMap.size()=${wMap.size()}"
	wMap[it[1]] = (wMap[it[1]] + applyTrans(wMap.remove(it[0]), transMap[it])).toSet()
	println "wMap[it[1]].size()=${wMap[it[1]].size()}"
}

def scaners = wMap.values().toList()[0]

//def scaners = [[62, 64, 1062], [-2502, -1099, -2536], [-1234, 145, 1220]]
println "scaners=${scaners}"
println (([scaners, scaners].combinations().collect { ([it[0], it[1]].transpose().collect { Math.abs(it[0] - it[1]) }).sum() }).max())
