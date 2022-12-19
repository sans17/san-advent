package us.ligusan.advent2022.d18

lines = new File(getClass().getResource('input.txt').toURI()).collect { it }

cs = lines.collect { it.split(',').collect { Integer.valueOf(it) } }
//println "cs=$cs"

cube = (1..3).collect { -1..1 }.combinations().findAll { it.findAll { it != 0 }.size() == 1 }
println "cube=$cube"
//cubeAll = (1..3).collect { -1..1 }.combinations()
//println "cubeAll=cubeAll"

ns0 = (cs.collectMany { c -> cube.collect { [it, c].transpose()*.sum() } } -cs) as Set
println "ns0=$ns0"

xs = ns0.collect { it[0] }
ys = ns0.collect { it[1] }
zs = ns0.collect { it[2] }
minMaxs = [[xs.min(), xs.max()], [ys.min(), ys.max()], [zs.min(), zs.max()]]
println "minMaxs=$minMaxs"

outside = ns0.findAll { n -> minMaxs.withIndex().find { minMax, ind -> minMax.contains(n[ind]) } != null  } as Set
println "outside=$outside"

for(i = 0;; i++) {
	newOutside = (outside + outside.collectMany { c -> cube.collect { [it, c].transpose()*.sum() } }.findAll { n -> minMaxs.withIndex().every { minMax, ind -> minMax[0] <= n[ind] && minMax[1] >= n[ind] } } - cs) as Set
	if(newOutside == outside) break
	outside = newOutside
	println "i=$i, outside.size=${outside.size()} outside=$outside"
}

inside = ns0 - outside
println "inside=$inside"

count = cs.collect { c ->
	ns = cube.collect { [it, c].transpose()*.sum() }
	s = ns.findAll { !cs.contains(it) && !inside.contains(it) }.size()
	println "c=$c, ns=$ns, s=$s"
	s
}.sum()
println "count=$count"
