package us.ligusan.advent2021.d2

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int x = 0
int depth = 0
int aim = 0

for(String element : list) {
	def split = (element =~ /(forward|up|down) (\d+)/)[0]

	int d = Integer.parseInt(split[2])

	switch(split[1]) {
		case 'forward':
			x += d
			depth += aim * d
			if(depth < 0) depth = 0
			break
		case 'down':
			aim += d
			break
		case 'up':
			aim -= d
	}

	//	println "split=${split}"
	//	println "x=${x}, depth=${depth}"
}
println "x=${x}, depth=${depth}, m=${x*depth}"
