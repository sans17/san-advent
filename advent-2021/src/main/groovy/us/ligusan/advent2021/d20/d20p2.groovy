package us.ligusan.advent2021.d20

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

String algoLine
def active = [:]

int i = -1
list.each { line ->
	if(line == '') i = 0
	else if(i < 0) algoLine = line
	else {
		active += line.toCharArray().toList().withIndex().collectEntries { activeChar, charIndex -> [[charIndex, i], activeChar == '#' ? '1' : '0' ]}
		i++
	}
}
//println "algoLine.size=${algoLine.size()}"
println "active=${active}"

def cube = ((1..2).collect { -1..1 }).combinations()
println "cube=${cube}"

def neis = []
(1..50).each { step ->
	def oldNeis = neis
	neis = (active.keySet().collectMany { activePoint ->
		cube.collect { [activePoint, it].transpose()*.sum() }
	}).toSet()
//	println "neis=${neis}"

	active = neis.collectEntries { point ->
		String neiIndex = (cube.collect {
			def neiP2 = [point, it].transpose()*.sum()
			active[neiP2] ?: step%2 == 0 && algoLine[0] == '#' && !oldNeis.contains(neiP2) ? '1' : '0'
		}).join()
//		println "point=${point}, neiIndex=${neiIndex}"

		[point, algoLine[Integer.valueOf(neiIndex, 2)] == '#' ? '1' : '0']
	}

	def one = active.findAll { it.value == '1' }
	println "step=${step}, one.size=${one.size()}"
	//	println "active.size=${active.size()}"
}