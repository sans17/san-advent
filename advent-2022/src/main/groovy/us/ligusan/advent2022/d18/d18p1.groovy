package us.ligusan.advent2022.d18

lines = new File(getClass().getResource('input.txt').toURI()).collect { it }

cs = lines.collect { it.split(',').collect { Integer.valueOf(it) } }
//println "cs=$cs"

cube = (1..3).collect { -1..1 }.combinations().findAll { it.findAll { it != 0 }.size() == 1 }
println "cube=$cube"

count = cs.collect { c -> 
	ns = cube.collect { [it, c].transpose()*.sum() }
	s = ns.findAll { !cs.contains(it) }.size()
	println "c=$c, ns=$ns, s=$s"
	s
}.sum()
println "count=$count"