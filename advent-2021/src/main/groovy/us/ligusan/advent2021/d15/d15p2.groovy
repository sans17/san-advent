package us.ligusan.advent2021.d15

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def m1 = list.collect { line -> line.collect { Integer.valueOf(it)
	} }
//m1.each { println it.join() }
//println '---------------------'

def m0 = (0..4).collectMany { timesY ->
	m1.collect { row ->
		(0..4).collectMany { timesX ->
			row.collect {
				int newValue = it + timesY + timesX
				newValue >= 10 ? newValue - 9 : newValue
			}
		}
	}
}
//m0.each { println it.join() }

int ySize = m0.size()
int xSize = m0[0].size()

def cube = ((1..2).collect { -1..1 }).combinations().findAll {
	it[0] ? !it[1] : it[1]
}
println cube

def m = m0.collect { it.collect { 0 } }
m[-1][-1] = m0[-1][-1]

def nei = [[xSize -1, ySize -1]]

for(int i = 0 ; m[0][0] == 0 ; i++) {
//	println "i=${i}, nei.size=${nei.size()}, ${(m.collect { it.count { it == 0 } }).sum()}"
//	println "i=${i}, nei=${nei}"
	def next = nei.pop()
	println "i=${i}, nei.size=${nei.size()}, next=${next}, ${m[next[1]][next[0]]}"
	
	nei += cube.findResults { 
		int nX = next[0] + it[0]
		int nY = next[1] + it[1]
		if(nX >= 0 && nX < xSize && nY >= 0 && nY < ySize && m[nY][nX] == 0) {
			m[nY][nX] = m[next[1]][next[0]] + m0[nY][nX]
			
			return [nX, nY]
		}
		return null
	}

	nei = nei.sort { m[it[1]][it[0]] }
}
println m[0][0] - m0[0][0]
