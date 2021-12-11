package us.ligusan.advent2021.d11

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int ySize = list.size()
int xSize = list[0].size()

def d = list.collect { line ->
    line.toCharArray().toList().collect() { Integer.valueOf(it.toString()) }
}
println d

def cube = ((1..2).collect { -1..1 }).combinations()
println cube

int i = 0
for(; (d.collect { it.sum() }).sum() > 0 ; i++){
//    println "--- ${it}"
    
    d = d.collect { it.collect { it + 1 } }
//    println "d=${d}"
    
    def curF = [] as Set
    def newF = d.withIndex().collectMany { line, y ->
        line.withIndex().findResults { e, x ->
            e > 9 ? [x,y] : null
        }
    }
    while(!newF.empty) {
//        println "curF=${curF}"
//        println "newF=${newF}"
        
        curF += newF

        newF = newF.collectMany { xy ->
            cube.findResults {
                int nX = xy[0] + it[0]
                int nY = xy[1] + it[1]

                return nX >= 0 && nX < xSize && nY >= 0 && nY < ySize ? [nX, nY] : null
            }
        }
        newF.removeAll(curF)
//        println "newF=${newF}"
        newF.each { d[it[1]][it[0]]++ }
        newF = (newF.findAll { d[it[1]][it[0]] > 9 }).toSet()
    }
    
    d = d.collect { it.collect { it > 9 ? 0 : it } }
    
//    println "d=${d}"
}
println i