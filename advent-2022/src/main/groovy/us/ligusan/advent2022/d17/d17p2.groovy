package us.ligusan.advent2022.d17

line = new File(getClass().getResource('input.txt').toURI()).collect { it }[0]
ls=line.size()

h = -BigInteger.ONE
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

m=[:]

ss0=[]
ss1=[]

j=0
i=0L
imax=new BigInteger('1000000000000')
for(; i<imax; i++) {
//for(i=0L; i<2022L; i++) {
	ii=i%frs.size()

	ws = []
			
	mi = m[ii]
	if(mi == null) m[ii] = (mi=[:])
	j0 = j
	ji = (0..3).collect { line[(j+it)%ls] }
	mj=mi[ji]

	fr = frs[ii].collect { [[h+1, 2+(mj?:0)], it].transpose()*.sum()  }
	if(mj == null) {
//		println "j=$j, fr=$fr"
		yd = 0
		for(k=0; k<4; k++) {
			ws << (w = ji[k])
			j=(j+1)%ls

			ydk = w == '>' ? 1 : -1
			frn = fr.collect { [it, [0, ydk]].transpose()*.sum() }
//					println "$w: frn=$frn"
			if(frn.find { it[1] < 0 || it[1] > 6 } == null) {
				fr = frn
				yd += ydk
			}
		}
//				println "yd=${yd}"
//				mi[ji] = yd
//				
//				println "i=$i, m=$m"
	} else {
		ws += ji
		j=(j+4)%ls
	}

	dx = 0			
	for(;;) {
//		println "j=$j, fr=$fr"
		frn = fr.collect { [it, [-1, 0]].transpose()*.sum() }
//		println "frn=$frn"
		if(frn.find { it[0] < 0 || sr.contains(it) } == null) fr = frn
		else {
			sr += fr
			break
		}

		w = line[j]
		j=(j+1)%ls
		ws << w
		
		frn = fr.collect { [it, [0, w == '>' ? 1 : -1]].transpose()*.sum() }
//		println "$w: frn=$frn"
		if(frn.find { it[1] < 0 || it[1] > 6 || sr.contains(it) } == null) {
			fr = frn
			dx++
		}
	}
//	println "i=$i, sr=$sr"
	
	h1 = sr.collect { it[0] }.max()
	dh = h1 - h
	h = h1
	
//	mins = (0..6).collect { y ->
//		c = sr.findAll { it[1] == y }
//		c.empty ? -1 : c.collect { it[0] }.max()
//	}
//	min = mins.min()
//	sr = sr.findAll { it[0] >= min }
	if(i%100_000 == 0) {
		println "i=$i, h=$h, sr.size=${sr.size()}"
		println "date=${new Date().toString()}"
//		println "min=$min, mins=$mins "
//		println "sr=$sr"
	}
	
	ns = [ii, j0, dh, dx]
	ss1 << ns
	ind = ss0.findLastIndexOf { it == ns }
	ss1Size = ss1.size()
	if(ind < 0 || ss0[(ind-ss1Size+1)..ind] != ss1) {
//		if(ss1Size == 13) {
//			println "ind=$ind"
//			println "ns=$ns"
//			println "ss0.sublist=${ss0[(ind-ss1Size+1)..ind]}"
//			println "ss1=$ss1"
//			
//			break
//		}

		
		ss0 += ss1
		ss1 = []
	} else {
//		if(ss1Size > 11) {
//			println "ind=$ind"
//			println "ns=$ns"
//			println "ss0.size=${ss0.size()}, ss1.size=${ss1.size()}"
//		}
		if(ind == ss0.size()-1) {
			println "ss0.size=${ss0.size()}, ss1.size=${ss1.size()}"
			println "ss0.sublist=${ss0[(ind-ss1Size)..ind]}"
			println "ss1=$ss1"
			
//		(h..0).each { x -> print "|${(0..6).collect { sr.findAll { it[0] == x }.collect { it[1] }.contains(it) ? '#' : ' ' }.join()}|\n" }
		
			break
		}
	}
}

sum = ss1.collect { it[2] }.sum()
println "i=$i, h=$h, sum=$sum"

numRem = (imax-i-1).divideAndRemainder(ss1.size())
rem = numRem[1]
println "numRem=$numRem"

h = h + numRem[0] * sum + (rem == 0 ? 0 : ss1[0..rem-1].collect { it[2] }.sum())

println h+1
