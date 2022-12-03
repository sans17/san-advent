package us.ligusan.advent2022.d1

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

max = [0, 0, 0]

def fixMax(c) {
	for (int i in 0..2) {
		if(max[i] <= c) {
			s = c
			c = max[i]
			max[i] = s
		}
	}
}

count = 1
current = 0
list.each { line ->
	if (line.isBlank()) {
		fixMax(current)
		println "${count++}: ${max}"
		current = 0
	} else current += new BigInteger(line)
}

println "${count++}: ${max}"
fixMax(current)

println "max=${max}"
println "${max.sum()}"
