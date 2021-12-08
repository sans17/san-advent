package us.ligusan.advent2021.d6

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def ageMap = [:]
(list[0].split(',').collect { Integer.valueOf(it) }).each {
	Integer num = ageMap[it]
	ageMap[it] = num == null ? 1 : num+1
}

println ageMap

(1..80).each {
	def newAgeMap = [:]
	ageMap.each { key, value ->
		(key == 0 ? [6, 8]: [key-1]).each {
			def n = newAgeMap[it]
			newAgeMap[it] = n == null ? value : n+value
		}
	}
	ageMap = newAgeMap

//	println "it=${it}, ageMap=${ageMap}"
}

println ageMap.values().sum()