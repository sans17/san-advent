package us.ligusan.advent2022.d17

line = new File(getClass().getResource('input.txt').toURI()).collect { it }[0]

h = -1
sr = []

frs = [
	(0..3).collect { [0, it] },
	[0..2, 0..2].combinations().findAll { it.find { it == 1 } != null },
	[0..2, 0..2].combinations().findAll { it[0] == 0 || it[1] == 2 },
	(0..3).collect { [it, 0] },
	[0..1, 0..1].combinations()
]
frs.each {
	println it
}

j=0
for(i=0; i<2022; i++) {
	fr = frs[i%frs.size()].collect { [[h+4, 2], it].transpose()*.sum()  }
	
	for(;;) {
//		println "j=$j, fr=$fr"

		w = line[j]
		j=(j+1)%line.size()
		frn = fr.collect { [it, [0, w == '>' ? 1 : -1]].transpose()*.sum() }
//		println "$w: frn=$frn"
		if(frn.find { it[1] < 0 || it[1] > 6 || sr.contains(it) } == null) fr = frn
		
		frn = fr.collect { [it, [-1, 0]].transpose()*.sum() }
//		println "frn=$frn"
		if(frn.find { it[0] < 0 || sr.contains(it) } == null) fr = frn
		else {
			sr += fr
			break
		}
	}
//	println "i=$i, sr=$sr"
	
	h = sr.collect { it[0] }.max()
	println "i=$i, h=$h"
}

println h+1
