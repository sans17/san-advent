package us.ligusan.advent2021.d8

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def t1 = list.collect { line ->
	println line

//	def t2 = (line.split())[10..-1]
	def t2 = ((line.split())[10..-1].collect { it.size() }).findAll() { [2,4,3,7].contains(it) }
	println t2
	t2.size()
}

println t1.sum()