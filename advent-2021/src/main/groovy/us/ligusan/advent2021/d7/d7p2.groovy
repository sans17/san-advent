package us.ligusan.advent2021.d7

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def hMap = [:]
(list[0].split(',').collect { Integer.valueOf(it) }).each { hMap[it] = (hMap[it] ?: 0) + 1 }

println hMap

println (((hMap.keySet().min()..hMap.keySet().max()).collect { h -> (hMap.collect { key, value ->
	def d = Math.abs(h - key)
	d * (d+1) / 2 * value
}).sum() }).min())
