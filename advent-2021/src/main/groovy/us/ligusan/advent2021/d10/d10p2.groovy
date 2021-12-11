package us.ligusan.advent2021.d10

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def score = 0
def r = list.findResults { line ->
	def st = []
	loop: for(char c : line.toCharArray()) switch(c) {
		case '(':
		case '[':
		case '{':
		case '<':
			st.push(c)
			break

		case ')':
		case ']':
		case '}':
		case '>':
			Character o = st.pop()
			if(c == ')' && '(' != o || c == ']' && '[' != o || c == '}' && '{' != o || c == '>' && '<' != o) return null
	}

	BigInteger sc = 0
	st.each { c ->
		sc *= 5
		switch(c) {
			case '(':
				sc += 1
				break
			case '[':
				sc += 2
				break
			case '{':
				sc += 3
				break
			case '<':
				sc += 4
		}
	}
	return sc
}

println r

r = r.sort()

println r

println r[(r.size() - 1)/2]