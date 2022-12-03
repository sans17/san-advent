package us.ligusan.advent2022.d2

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

s=['A X':4, 'A Y':8, 'A Z':3, 'B X':1, 'B Y':5, 'B Z':9, 'C X':7, 'C Y':2, 'C Z':6]

score = 0
list.each { line ->
	score += s[line]
	println "line=${line}, s[line]=${s[line]}, score=${score}"
}
println score
