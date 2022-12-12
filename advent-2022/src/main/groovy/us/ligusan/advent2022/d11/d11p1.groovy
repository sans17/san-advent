package us.ligusan.advent2022.d11

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

ms = list.collate(7).collect { lines ->
	i = (lines[0] =~ /Monkey (\d+):/)[0][1]
	items = (lines[1] =~ /Starting items: (.+)/)[0][1].split(', ').collect { it }
	operation = (lines[2] =~ /Operation: new = (.+)/)[0][1]
	testNumber = Integer.valueOf((lines[3] =~ /Test: divisible by (\d+)/)[0][1])
	trueAction = Integer.valueOf((lines[4] =~ /\s*If true: throw to monkey (\d+)/)[0][1])
	falseAction = Integer.valueOf((lines[5] =~ /\s*If false: throw to monkey (\d+)/)[0][1])
	
	ret = [items, operation, testNumber, trueAction, falseAction, 0]
	
	println "i=$i, ret=$ret"
	
	return ret
}

println ms

(1..20).each {
	ms.each { m ->
//		println "m=$m"
		m[0].each { i ->
			i1 = Eval.me(m[1].replaceAll('old', i)).intdiv(3)
//			println "i=$i, i1=$i1"
			ms[m[i1%m[2] == 0 ? 3 : 4]][0] << i1.toString()
			
			m[5]++
		}
		m[0].clear()
	}
}
//println "${ms.collect { it[5] }}"

mss = ms.collect { it[5] }.sort { -it }

println mss[0] * mss[1]

