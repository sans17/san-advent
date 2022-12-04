package us.ligusan.advent2022.d4

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def boolean overlap(a, b) {
	return a.max() >=  b.min() && a.min() <= b.max() ;
}

count = 0
list.each { line ->
    split = (line =~ /(\d+)-(\d+),(\d+)-(\d+)/)[0]
	println split
	
	a = split[1..4].collect({ new BigInteger(it)})
	
	
	if(overlap(a[0..1], a[2..3])) {
		println "a=$a"
		count++
	}
}
println count
