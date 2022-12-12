package us.ligusan.advent2022.d12

list = new File(getClass().getResource('input.txt').toURI()).collect { Arrays.asList(it.toCharArray()) }

xSize = list.size()
ySize = list[0].size()

list.each { println it.join() }
println '--------------------'

p = []

m = list.withIndex().collect { row, x -> row.withIndex().collect { element, y ->
	if(element == 'E') {
		p << [x, y]
		return 0
	}
	return -1
} }

//m.each { println it }

cube = (1..2).collect { -1..1 }.combinations().findAll { it[0] == 0 ^ it[1] == 0 }
//println cube
cube1 = (1..2).collect { -1..1 }.combinations().collect { it.reverse() }.collate(3)
//println cube1

res = 0
for(i=0; res==0; i++) {
//	println "p=$p"
	
	xy = p.pop()
	v = list[xy[0]][xy[1]]
	if(v == 'E') v = 'z'
//	println "xy=$xy, v=$v"
	
	pr0 = cube1.collect { row ->
		row.collect {
			n = [xy, it].transpose()*.sum()
			
			n[0] >= 0 && n[0] < xSize && n[1] >= 0 && n[1] < ySize ? list[n[0]][n[1]] : ' '
		}
	}
	pr0.each { println it.join() }
	println '----'

	pr1 = cube1.collect { row ->
		row.collect {
			n = [xy, it].transpose()*.sum()
			
			n[0] >= 0 && n[0] < xSize && n[1] >= 0 && n[1] < ySize ? m[n[0]][n[1]] : -2
		}
	}
	pr1.each { println it }
	println '----'

	for(c in cube) {
		n = [xy, c].transpose()*.sum()
		if(n[0] >= 0 && n[0] < xSize && n[1] >= 0 && n[1] < ySize) {
			nv = list[n[0]][n[1]]
			startFlag = nv == 'S'
			if(startFlag) nv = 'a'
//			println "n=$n, nv=$nv"
			
			if(m[n[0]][n[1]] == -1 && (int)v - (int)nv <= 1) {
				m[n[0]][n[1]] = m[xy[0]][xy[1]] + 1
				
				if(nv == 'a') {
					res = m[n[0]][n[1]]
					break
				}
				
				p << n
			}
		}
	}

	pr1 = cube1.collect { row ->
		row.collect {
			n = [xy, it].transpose()*.sum()
			
			n[0] < xSize && n[1] < ySize ? m[n[0]][n[1]] : -2
		}
	}
	pr1.each { println it }
	println '----'

//	m.each { println it }
	println "p=$p"
	println "${p.collect { m[it[0]][it[1]] }}"
	println '--------------------'
}
println res
