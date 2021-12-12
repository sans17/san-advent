package us.ligusan.advent2021.d12

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def paths = [:]
list.each { line ->
    def split = (line =~ /(\w+)-(\w+)/)[0]

    println split

    (1..2).each {
        def newP = paths[split[it]] ?: []
        newP << split[3 - it]
        paths[split[it]] = newP
    }
}
//println paths

def fullP = []

def currP = [['start']]
while(!currP.empty) {
    def next = currP.pop()
    if(next[-1] == 'end') fullP << next
    else {
        currP += paths[next[-1]].findResults {
            if(Character.isLowerCase(it.charAt(0)) && next.contains(it)) return null
            def newP = next.collect { it }
            newP << it
            return currP.contains(newP) ? null : newP
        }

//        println "currP=${currP}"
    }
}

//println fullP
println fullP.size()
