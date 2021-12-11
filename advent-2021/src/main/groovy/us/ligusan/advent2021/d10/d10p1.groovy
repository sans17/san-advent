package us.ligusan.advent2021.d10

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int score = 0
list.each { line ->
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
			int d = 0
			Character o = st.pop()
			if(c == ')' && '(' != o || c == ']' && '[' != o || c == '}' && '{' != o || c == '>' && '<' != o) switch(c) {
				case ')':
					d = 3
					break
				case ']':
					d = 57
					break
				case '}':
					d = 1197
					break
				case '>':
					d = 25137
			}
			if(d > 0) {
				score += d
				break loop
			}
	}
}

println score