package us.ligusan.advent2021.d16

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def line = (list[0].collect { Integer.toBinaryString(Integer.valueOf(it, 16)).padLeft(4, '0') }).join()
println line

def process(int start, Integer num, String input, tree) {
	println "start=${start}, num=${num}, input=${input}"

	while(num == null && start+6 < input.size() || num > 0) {
		println "start=${start}, num=${num}, input.size=${input.size()}"

		String v = input[start..start+2]
		String t = input[start+3..start+5]
		println "v=${v}, t=${t}"

		if(t=='100') {
			tree << v

			start+=6

			for(boolean last = false; !last; start++) {
				String r = input[start..(start+=4)]
				println "r=${r}"

				last = r[0] == '0'
			}
		} else {
			String i = input[start+6]
			if(i == '0') {
				def l = new BigInteger(input[start+7..start+21], 2)
				println "l=${l}"

				def nTree = []
				process(0, null, input[start+22..(start+=21+l)], nTree)
				println "nTree=${nTree}"
				
				tree << v << nTree
				start++
			} else {
				def n = new BigInteger(input[start+7..start+17], 2)
				println "n=${n}"

				def nTree = []
				start = process(start+18, n, input, nTree)
				println "nTree=${nTree}"

				tree << v << nTree
			}
		}

		if(num != null) num--
	}
	
	println "start=${start}"
	return start
}

def tree = []
int end = process(0, null, line, tree)
println "end=${end}, tree=${tree}"
println ((tree.flatten().collect { Integer.valueOf(it, 2) }).sum())
