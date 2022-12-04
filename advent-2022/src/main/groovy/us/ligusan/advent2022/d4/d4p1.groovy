package us.ligusan.advent2022.d4

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def boolean contains(a1, a2, b1, b2) {
	return a1 <= b1 && a1 <= b2 && b1 <= a2 && b2 <= a2 ;
}

count = 0
list.each { line ->
    split = (line =~ /(\d+)-(\d+),(\d+)-(\d+)/)[0]
	println split
	
	a = split[1..4].collect({ new BigInteger(it)})
	
	
	if(contains(a[0], a[1], a[2], a[3]) || contains(a[2], a[3], a[0], a[1])) {
		println "a=$a"
		count++
	}
}
println count
