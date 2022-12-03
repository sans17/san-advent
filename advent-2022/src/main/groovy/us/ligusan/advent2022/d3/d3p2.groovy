package us.ligusan.advent2022.d3

list = new File(getClass().getResource('input.txt').toURI()).collect { Arrays.asList(it.toCharArray()) }

psum = 0
list.collate(3).each { 
	c = it[0].intersect(it[1]).intersect(it[2]) as Set
	println c
	
	psum += c.sum { it >= 'a' && it <= 'z' ? (int)it - (int)'a' + 1 : (int)it - (int)'A' + 27 }
}
println "psum=${psum}"
