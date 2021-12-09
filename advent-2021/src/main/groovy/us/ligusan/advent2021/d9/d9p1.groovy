package us.ligusan.advent2021.d9

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int ySize = list.size()
int xSize = list[0].size()

def d = list.collect { line ->
	line.toCharArray().toList().collect() { Integer.valueOf(it.toString()) }
}
println d

def cube = ((1..2).collect { -1..1 }).combinations().findAll {
	it[0] ? !it[1] : it[1]
}
println cube

def t1 = d.withIndex().collect { line, y ->
//	println "y=${y}, line=${line}"
	
	def t3 = line.withIndex().findAll { depth, x ->
//		println "x=${x}, depth=${depth}"
		
		def t2 = cube.find {
			int nX = x + it[0]
			int nY = y + it[1]
//			println "nX=${nX}, nY=${nY}"
			
			nX >= 0 && nX < xSize && nY >= 0 && nY < ySize && d[nY][nX] <= d[y][x]
		}
//		println t2
		
		t2 == null
	}
	
	println "y=${y}, t3=${t3}"
	
	t3.collect { it[0] + 1 }
}
println t1.flatten().sum()