package us.ligusan.advent2021.d21

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def m = [:]
list.each { line ->
	def split = (line =~ /Player (\d+) starting position: (\d+)/)[0]
	m[Integer.valueOf(split[1])] = Integer.valueOf(split[2])
}
println m

int player = 1
int count = 0
int die = 1

def score = m.collectEntries { key, value -> [key, 0]}
println score
while(true) {
	count += 3
	int newP = (m[player] + ((1..3).collect {
		int currentDie = die++
		if(die > 100) die = 1
		return currentDie
	}.sum())) % 10
	if(newP == 0) newP = 10
	m[player] = newP
	score[player] += newP
	println "count=${count}, m=${m}, score=${score}"

	if(score[player] >= 1000) break

	player = 3 - player
}
println "count=${count}, player=${player}, score=${score}"
println count*score[3-player]
