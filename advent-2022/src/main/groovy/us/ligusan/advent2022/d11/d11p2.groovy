package us.ligusan.advent2022.d11

ms = [
	[ [93, 98], { n -> n*17 }, 19, [5, 3], BigInteger.ZERO ],
	[ [95, 72, 98, 82, 86], { n -> n+5 }, 13, [7, 6], BigInteger.ZERO ],
	[ [85, 62, 82, 86, 70, 65, 83, 76], { n -> n+8 }, 5, [3, 0], BigInteger.ZERO ],
	[ [86, 70, 71, 56], { n -> n+1 }, 7, [4, 5], BigInteger.ZERO ],
	[ [77, 71, 86, 52, 81, 67], { n -> n+4 }, 17, [1, 6], BigInteger.ZERO ],
	[ [89, 87, 60, 78, 54, 77, 98], { n -> n*7 }, 2, [1, 4], BigInteger.ZERO ],
	[ [69, 65, 63], { n -> n+6 }, 3, [7, 2], BigInteger.ZERO ],
	[ [89], { n -> n * n }, 11, [0, 2], BigInteger.ZERO ]
]

ms.each { m -> m[0] = m[0].collect { new BigInteger(it.toString()) } }
println ms

di = ms.inject(BigInteger.ONE) { r, m -> r * m[2] }
println "di=$di"

(1..10_000).each { round ->
	println round
	ms.eachWithIndex { m, i ->
//		println "i=$i"
//		println "m=$m"
		m[0].each { n ->
//			println "n=$n"
			n1 = m[1](n)
			nm = m[3][n1.remainder(m[2]) == 0 ? 0 : 1]
			n2 = n1.remainder(di)
			
//			println "n=$n, n1=$n1, nm=$nm, n2=$n2"
			ms[nm][0] << n2
		}
		m[4] += m[0].size()
		m[0].clear()
	}
//	println "${ms.collect { it[0] }}"
}
//println ms

println "${ms.collect { it[4] }}"

mss = ms.collect { it[4] }.sort { -it }

println mss[0] * mss[1]
