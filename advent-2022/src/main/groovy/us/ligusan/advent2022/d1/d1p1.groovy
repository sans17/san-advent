package us.ligusan.advent2022.d1

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

count = 1
max = 0
current = 0
list.each { line ->
	if (line.isBlank()) {
		println "${count++}: ${current}"
		if(max <= current) max = current
		current = 0
	} else current += new BigInteger(line)
}

println "${count++}: ${current}"
if(max <= current) max = current

println "max=${max}"