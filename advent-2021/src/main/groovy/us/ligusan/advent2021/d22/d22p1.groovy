package us.ligusan.advent2021.d22

def list = new File(getClass().getResource('example.txt').toURI()).collect { it }

def areas = []
list.each { line ->
	if(line.charAt(0) != '#') {
		def split = (line =~ /(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)/)[0]
		areas << [split[1], split[2..-1].collect { Integer.valueOf(it) }]
	}
}

def dots = [] as Set
areas.each { area ->
	println "area=${area}"

	int x0 = Math.max(-50, area[1][0])
	int x1 = Math.min(50, area[1][1])
	if(x0 <= x1) {
		int y0 = Math.max(-50, area[1][2])
		int y1 = Math.min(50, area[1][3])
		if(y0 <= y1) {
			int z0 = Math.max(-50, area[1][4])
			int z1 = Math.min(50, area[1][5])
			if(z0 <= z1) {
				(x0..x1).each { x ->
					(y0..y1).each { y ->
						(z0..z1).each { z ->
							def dot =[x, y, z]
							if(area[0] == 'on') dots << dot
							else dots.remove(dot)
						}
					}
				}

				println dots.size()
			}
		}
	}
}
