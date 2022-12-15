package us.ligusan.advent2022.d15

list = new File(getClass().getResource('test.txt').toURI()).collect { it }

ss = []
bs = []

//y = 10
y=2_000_000
ys = [] as Set

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
	md = [s, bs[i]].transpose().collect { (it[0] - it[1]).abs() }.sum()
//	println "s=$s, bs[$i]=${bs[i]}, md=$md"
	
	yd=(s[1]-y).abs()
	if(yd <= md) {
		ys += (s[0]-(md-yd))..(s[0]+(md-yd))
//		println "ys=$ys"
	}

	println "i=$i, size=${ys.size()}"
}

ys.removeAll((ss + bs).findAll { it[1] == y }.collect { it[0] })
//println "ys=$ys"

println ys.size()
