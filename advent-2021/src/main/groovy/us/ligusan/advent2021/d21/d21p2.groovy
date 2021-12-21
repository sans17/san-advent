package us.ligusan.advent2021.d21

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def m0 = [:]
list.each { line ->
	def split = (line =~ /Player (\d+) starting position: (\d+)/)[0]
	m0[Integer.valueOf(split[1])] = Integer.valueOf(split[2])
}
println m0

cube = ((1..3).collect { 1..3 }).combinations()
print "cube=${cube}"

posScoreMap = [:]

def wCount(pos, score) {
	def scoreMap = posScoreMap[pos]
	if(scoreMap == null) posScoreMap[pos] = (scoreMap = [:])
	def ret = scoreMap[score]

	if(ret != null) return ret
	if(score[1] >= 21) ret = [BigInteger.ZERO, BigInteger.ONE]
	else {
		def a = cube.collect {
			int newP = (pos[0] + it.sum()) % 10
			if(newP == 0) newP = 10

			def c = wCount([pos[1], newP], [score[1], score[0] + newP])

//			println "pos=${pos}, score=${score}, it=${it}, newP=${newP}, c=${c}"
			return c
		}
//		println "a=${a}"

		ret = (a.transpose()*.sum()).reverse()

		scoreMap[score] = ret
	}

	println "pos=${pos}, score=${score}, ret=${ret}"
	return ret
}

println (wCount([m0[1], m0[2]], [0, 0]).max())
//println wCount([1, 1], [0, 0])
