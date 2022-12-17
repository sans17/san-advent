package us.ligusan.advent2022.d16

import java.lang.annotation.Retention

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

vs=[:]
maxActions = 30
ps=[:]

list.each { 
	println it
	split = (it =~ /Valve (\S+) has flow rate=(\d+); tunnels? leads? to valves? (.*)/)[0]
	println "split=$split"
	
	vs[split[1]] = [Integer.valueOf(split[2]), split[3].split(', ')]
}
println ''
println vs

nzv = vs.findAll { it.value[0] > 0 }.collect { it.key }
println ''
println "nzv=$nzv"
gps = nzv.collectEntries { a -> [a, nzv.findAll { a != it }.collectEntries { b -> [b, path(a, b, [])] }] }
gps.put('AA', nzv.collectEntries { b -> [b, path('AA', b, [])] })
println ''
println "gps=$gps"

def path(a, b, p) {
	ret = vs[a][1].contains(b) ? [b] : vs[a][1].findAll { !p.contains(it) }.findResults { 
		p1 = p.clone()
		p1 << a

		ret = path(it, b, p1)
		if(ret == null) return null
		
		ret.add(0, it)
		return ret
	}.max { -it.size() }
//	println "a=$a, b=$b, p=$p, ret=$ret"
	ret
}

def next(actions) {
//	println "actions=$actions"
	cl = actions[-1][0]
//	println "cl=$cl"

	ret = []
	
	released = actions.collect { it[0] }
//	println "released=$released"
//	println "${nzv - released}"
	
	ps = (nzv - released).collect { gps[cl][it] }
//	println "ps=$ps"
		
	ret = ps.collect {
//		println "it=$it"
		ac = actions.clone()
		ac << [it[-1], actions[-1][1]+it.size()+1 ]
//		println "ac=$ac"
		ac
	}.findAll { it[-1][1] < maxActions }
//	println "ret=$ret"
	return ret.empty ? null : ret
}

actions = [[['AA', 0]]]
factions = []
for(;;) {
	println ""
	println "factions.size=${factions.size()}"
	println "actions.size=${actions.size()}"
	
	if(actions.empty) break
	action = actions.pop()
	println "action=$action"
	
	n = next(action)
	println "n=$n"
	if(n == null) factions << action
	else actions += n
}
//println factions
//factions.each { println it.size() }

//factions.each {
//	println ""
//	println it
//	rs = it.collect { vs[it[0]][0] * (30 - it[1]) }.sum()
//	println rs
//}

m = factions.collect { it.collect { vs[it[0]][0] * (maxActions - it[1]) }.sum() }.max()
println m
	
//println ''
//println actions
