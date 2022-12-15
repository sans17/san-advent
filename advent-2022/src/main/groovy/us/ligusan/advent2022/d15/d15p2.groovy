package us.ligusan.advent2022.d15

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def md(s, p) {
	return [s, p].transpose().collect { (it[0] - it[1]).abs() }.sum()
}

ss = []
bs = []
mds = []

//max = 20
max = 4_000_000

list.eachWithIndex { line, i ->
	println "i=$i, line=$line"
	split = (line =~ /Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)/)[0]
	cs = split[1..4].collect { Integer.valueOf(it) }
	
	ss << cs[0..1]
	bs << cs[2..3]
}
println ss
println bs

ss.eachWithIndex { s, i ->
	mds << md(s, bs[i])
}
println mds

cube = (1..2).collect { [-1, 1] }.combinations()
println cube

outer: for(j=0; j<ss.size(); j++) {
	s=ss[j]
	println "j=$j, s=$s, mds[$j]=${mds[j]}"
	for(d=0; d<=mds[j]+1; d++) {
		[]
		
		ps = cube.collect { [it, [d, (mds[j]+1)-d]].transpose().collect { it[0] * it[1] } }.collect { [s, it].transpose()*.sum() }.findAll { it.find { it < 0 || it > max } == null } as Set
//		println "ps=$ps"
		for(p in ps) {
//			println "p=$p"
			ind = ss.withIndex().findIndexOf { s1, i ->
				d1 = md(s1, p)
//				println "i=$i, s1=$s1, mds[$i]=${mds[i]}, d1=$d1" 
				d1 <= mds[i]
			}
//			println "ind=$ind"

			if(ind < 0) {
				println '-----'
				println p
				println new BigInteger(p[0])*4_000_000+p[1]
				break outer
			}
		}
	}
}

//println new BigInteger('2740279')*max+2_625_406