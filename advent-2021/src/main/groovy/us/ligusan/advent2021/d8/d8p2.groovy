package us.ligusan.advent2021.d8

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def t1 = list.collect { line ->
	println line

	def dMap = [:]
	
	def t2 = line.split().collect { it.toCharArray().toList().sort() }
	println t2
	
	def t3 = t2[0..9]
	dMap[1] = t3.find { it.size() == 2 }
	dMap[3] = t3.find { it.size() == 5 && it.containsAll(dMap[1]) }
	t3.remove(dMap[3])
	dMap[4] = t3.find { it.size() == 4 }
	dMap[6] = t3.find { it.size() == 6 && !it.containsAll(dMap[1]) }
	t3.remove(dMap[6])
	dMap[7] = t3.find { it.size() == 3 }
	dMap[8] = t3.find { it.size() == 7 }
	dMap[9] = t3.find { it.size() == 6 && it.containsAll(dMap[3]) }
	t3.remove(dMap[9])
	dMap[0] = t3.find { it.size() == 6 }
	dMap[5] = t3.find { it.size() == 5 && dMap[9].containsAll(it) }
	t3.remove(dMap[5])
	dMap[2] = t3.find { it.size() == 5  }
	println dMap

	def rMap = dMap.collectEntries { key, value -> [value, key] }
	println rMap
	
	Integer.valueOf(((t2[11..-1].collect { rMap[it].toString() }).join()))
}

println t1.sum()