package us.ligusan.advent2021.d15

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def m = list.collect { line -> line.collect { Integer.valueOf(it) } }
println m

int start = m[0][0]

int yMax = m.size()-1
(yMax..0).each { y ->
//	println "y=${y}, m[${y}]=${m[y]}"

	int xMax = m[y].size() - 1
	(xMax..0).each { x ->
//		println "x=${x}, m[${y}][${x}]=${m[y][x]}"
		
		m[y][x] += (x==xMax ? y==yMax ? 0 : m[y+1][x] : y==yMax ? m[y][x+1] : [m[y+1][x], m[y][x+1]].min())

//		println "x=${x}, m[${y}][${x}]=${m[y][x]}"
	}

//	println "y=${y}, m[${y}]=${m[y]}"
}
println m

println m[0][0] - start
