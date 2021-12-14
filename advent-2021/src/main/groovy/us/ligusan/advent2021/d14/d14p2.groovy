package us.ligusan.advent2021.d14

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

boolean part2 = false

String s
m = [:]

list.each { line ->
	if(part2) {
		def split = (line =~ /([A-Z]{2}) -> ([A-Z])/)[0]
		println split

		m[split[1]] = split[2]
	} else if(!(part2 = "" == line))
		s = line
}
println s
println m

cm = [:]

def Map freq(String start, int num) {
	//	println "start=${start}, num=${num}"

	def ret
	def cm1 = cm[start]
	if(cm1 == null) cm[start] = (cm1 = [:])
	ret = cm1[num]

	if(ret != null) return ret

	ret = [:]
	if(num == 0) {
		start.each {
			ret[it] = (ret[it] ?: BigInteger.ZERO) + 1
		}
	} else if(start.size() == 1) ret[start[0]] = 1
	else {
		def r1 = freq(start[0] + m[start[0..1]], num - 1)
		//		println "r1=${r1}"
		ret = r1.collectEntries { it }

		def r2 = freq(m[start[0..1]] + start[1], num - 1)
		//		println "r2=${r2}"
		r2.each { key, value -> ret[key] = (ret[key] ?: BigInteger.ZERO) + value }
		ret[m[start[0..1]]] = ret[m[start[0..1]]] - 1
//		println "ret=${ret}"

		def r3 = freq(start[1..-1], num)
		//		println "r3=${r3}"
		r3.each { key, value -> ret[key] = (ret[key] ?: BigInteger.ZERO) + value }
		ret[start[1]] = ret[start[1]] - 1
	}

	cm1[num] = ret

//	println "start=${start}, num=${num}, ret=${ret}"
	return ret
}

def c = freq(s, 40)

println c
def sc = c.values().sort()
println sc[-1] - sc[0]