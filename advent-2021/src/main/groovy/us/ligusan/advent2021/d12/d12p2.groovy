package us.ligusan.advent2021.d12

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def paths = [:]
list.each { line ->
    def split = (line =~ /(\w+)-(\w+)/)[0]

//    println split

    (1..2).each {
        if('start' != split[3 - it]) {
            def newP = paths[split[it]] ?: []
            newP << split[3 - it]
            paths[split[it]] = newP
        }
    }
}
println paths

int count = 0

def currP = [[',start,', false]]
while(!currP.empty) {
    def next = currP.pop()
//    println "next=${next}"
    
    if(next[0].endsWith(',end,')) count++
    else {
        int l = next[0].size()
        String lastC = next[0].substring(next[0].lastIndexOf(',', l-2) + 1, l - 1)
//        println "lastC=${lastC}"
        
        currP += paths[lastC].findResults { nextC ->
//            println "nextC=${nextC}"
            
            boolean firstFlag = Character.isLowerCase(nextC.charAt(0)) && next[0].indexOf(',' + nextC + ',') > 0
            return firstFlag && next[1] ? null : [ next[0] + nextC + ',', next[1] || firstFlag ] 
        }

//        println "currP=${currP}"
        println "${currP.size()}"
    }
}

println count
