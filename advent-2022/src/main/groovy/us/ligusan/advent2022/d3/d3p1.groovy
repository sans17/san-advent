package us.ligusan.advent2022.d3

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

psum = 0
list.each { line ->
	println line
	a = Arrays.asList(line.toCharArray())
	int s = a.size()
	a1 = a.subList(0, (int)(s/2))
	a2 = a.subList((int)(s/2), (int)(s))
	println "a1=${a1}, a2=${a2}"
	
	c = a1.intersect(a2) as Set
	println c
	
	psum += c.sum { it >= 'a' && it <= 'z' ? (int)it - (int)'a' + 1 : (int)it - (int)'A' + 27 }
}
println "psum=${psum}"
