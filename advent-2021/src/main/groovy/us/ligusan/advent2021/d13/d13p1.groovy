package us.ligusan.advent2021.d13

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def m = []
def f = []

boolean part2 = false

list.each { line ->
	if(part2) {
		def split = (line =~ /fold along (x|y)=(\d+)/)[0]
		println split

		f << [split[1], Integer.parseInt(split[2])]
	} else if(!(part2 = "" == line))
		m << line.split(',').collect { Integer.valueOf(it) }
}
println m
println f

for(def fc : f) {
	boolean xF = fc[0] == 'x'
	m = (m.findResults {
		int c = it[xF ? 0 : 1]
		int newC = c < fc[1] ? c : 2 * fc[1] - c

		return c == fc[1] ? null : [xF ? newC : it[0], xF ? it[1] : newC]
	}).toSet()
	
	break
}

println m
println m.size()
