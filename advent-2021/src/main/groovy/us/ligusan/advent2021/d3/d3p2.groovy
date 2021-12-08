package us.ligusan.advent2021.d3

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def og = list
def co = list
for(int i = 0; og.size() > 1; i++) {
	def ogMost = (og.collect {Integer.valueOf(it[i])}.sum() / og.size()).round().toString()
	og = og.findAll { it[i] == ogMost }

	if(co.size() > 1) {
		def coMost = (co.collect {Integer.valueOf(it[i])}.sum() / co.size()).round().toString()
		co = co.findAll { it[i] != coMost }
	}
}
println "og=${og}"
println "co=${co}"

println Integer.parseInt(og.join(), 2) * Integer.parseInt(co.join(), 2)
