package us.ligusan.advent2021.d16

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def line = (list[0].collect { Integer.toBinaryString(Integer.valueOf(it, 16)).padLeft(4, '0') }).join()
println line

def process(int start, Integer num, String input, tree) {
	println "start=${start}, num=${num}, input=${input}"

	while(num == null ? start+6 < input.size() : num > 0) {
		println "start=${start}, num=${num}, input.size=${input.size()}"

		String v = input[start..start+2]
		String t = input[start+3..start+5]
		println "v=${v}, t=${t}"

		if(t=='100') {
			start+=6

			String n = ""
			for(boolean last = false; !last; start++) {
				String r = input[start..(start+=4)]
				println "r=${r}"

				n += r[1..4]

				last = r[0] == '0'
			}
			println n

			tree << [ 'num', new BigInteger(n, 2) ]
		} else {
			def nTree = []

			switch(t) {
				case '000':
					nTree << '+'
					break
				case '001':
					nTree << '*'
					break
				case '010':
					nTree << 'min'
					break
				case '011':
					nTree << 'max'
					break
				case '101':
					nTree << '>'
					break
				case '110':
					nTree << '<'
					break
				case '111':
					nTree << '=='
			}

			String i = input[start+6]
			if(i == '0') {
				def l = new BigInteger(input[start+7..start+21], 2)
				println "l=${l}"

				process(0, null, input[start+22..(start+=21+l)], nTree)
				println "nTree=${nTree}"

				tree << nTree
				start++
			} else {
				def n = new BigInteger(input[start+7..start+17], 2)
				println "n=${n}"

				start = process(start+18, n, input, nTree)
				println "nTree=${nTree}"

				tree << nTree
			}
		}

		if(num != null) num--
	}

	println "start=${start}"
	return start
}

def tree = []
int end = process(0, 1, line, tree)
println "end=${end}, tree=${tree}"

def calc(exp) {
	switch(exp[0]) {
		case 'num': return exp[1]
		case '+': return (exp[1..-1].collect { calc(it) }).sum()
		case '*': return (exp[1..-1].inject(1) { m, it -> m * calc(it) })
		case 'min': return (exp[1..-1].collect { calc(it) }).min()
		case 'max': return (exp[1..-1].collect { calc(it) }).max()
		case '>': return calc(exp[1]) > calc(exp[2]) ? 1 : 0
		case '<': return calc(exp[1]) < calc(exp[2]) ? 1 : 0
		case '==': return calc(exp[1]) == calc(exp[2]) ? 1 : 0
	}
}
println calc(tree[0])

