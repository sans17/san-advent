package us.ligusan.advent2022.d2

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

s=['A X':4, 'A Y':8, 'A Z':3, 'B X':1, 'B Y':5, 'B Z':9, 'C X':7, 'C Y':2, 'C Z':6]
s1=['A X':s['A Z'], 'A Y':s['A X'], 'A Z':s['A Y'], 'B X':s['B X'], 'B Y':s['B Y'], 'B Z':s['B Z'], 'C X':s['C Y'], 'C Y':s['C Z'], 'C Z':s['C X']]

score = 0
list.each { line ->
	score += s1[line]
//	println "line=${line}, s[line]=${s[line]}, score=${score}"
}
println score
