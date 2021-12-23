package us.ligusan.advent2021.d22

def list = new File(getClass().getResource('example.txt').toURI()).collect { it }

def areas = []
list.each { line ->
	if(line.charAt(0) != '#') {
		def split = (line =~ /(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)/)[0]
		areas << [split[1] == 'on', split[2..-1].collect { Integer.valueOf(it) }]
	}
}
areas.each { println it }
println '---------------'

def intersect(l, r) {
	def ret = null
	
	int x0 = Math.max(l[1][0], r[1][0])
	int x1 = Math.min(l[1][1], r[1][1])
	if(x0 <= x1) {
		int y0 = Math.max(l[1][2], r[1][2])
		int y1 = Math.min(l[1][3], r[1][3])
		if(y0 <= y1) {
			int z0 = Math.max(l[1][4], r[1][4])
			int z1 = Math.min(l[1][5], r[1][5])
			if(z0 <= z1) {
				ret = [x0, x1, y0, y1, z0, z1]
			}
		}
	}
	println "l=${l}, r=${r}, ret=${ret}"
	
	return ret
}

def count(list, onof) {
	println "list=${list}, onof=${onof}"
	if(list.size() == 1) return 0
	
	

	def subList = list.collect { it }
	subList.removeLast()
	
	def ret = count(subList, onof)
	def inter = subList.collect {
		println "it=${it}"
		(it[0] == list[-1][0] ? -1 : 1) * count(intersect(it, list[-1]))
	}
	println "inter=${inter}"
	ret += inter.sum()
	if(list[-1][0] == onof) {
		def ll = ((list[-1][1].collate(2).collect { Math.abs(it[1] - it[0] + 1) }).inject(1) { m, n -> m*n })		
		println "list[-1]=${list[-1]}, ll=${ll}"
		ret += ll
	}
	
	println "list=${list}, onof=${onof}, ret=${ret}"
	return ret 
}

println count(areas, true)
