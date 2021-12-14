package us.ligusan.advent2021.d14

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

boolean part2 = false

String s
def m = [:]

list.each { line ->
	if(part2) {
		def split = (line =~ /([A-Z]{2}) -> ([A-Z])/)[0]
		println split

		m[split[1]] = split[2]
	} else if(!(part2 = "" == line))
		s = line
}
println s
println m

(1..10).each {
	String newS = s[0]
	
	(0..s.size()-2).each { 
		newS += (m[s.substring(it, it+2)] ?: "") + s[it+1]
	}
	
	s = newS
//	println s
}

def c = [:]
s.each { 
	c[it] = (c[it] ?: 0) + 1
}
println c
def sc = c.values().sort()
println sc[-1] - sc[0]