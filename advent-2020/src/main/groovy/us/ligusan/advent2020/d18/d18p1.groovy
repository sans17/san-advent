package us.ligusan.advent2020.d18;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {
    it
}

def String flatEval(String statement) {
    String newStatement = statement
    for(def matcher ; (matcher = newStatement =~ /(\d+) ([\+\*]) (\d+)/).find() ;) {
        //        println " 0: ${newStatement} ${matcher[0]}"
        def i = new BigInteger(matcher[0][1])
        def j = new BigInteger(matcher[0][3])

        def res = matcher[0][2] == '+' ? i + j : i * j
        newStatement = matcher.replaceFirst(res.toString())
    }

    newStatement
}

BigInteger sum = 0
list.withIndex().each { line, i->
    //    String line = list[i]
    //    println "${i} ${line}"

    String newLine = line
    for(def matcher ; (matcher = newLine =~ /\(([^\(\)]+)\)/).find() ;) {
        //        println "10: ${newLine} ${matcher[0]}"
        newLine = matcher.replaceFirst(flatEval(matcher[0][1]))
    }

    String res = flatEval(newLine)
    sum += new BigInteger(res)
    println "${i} -> ${line} = ${res}; ${sum}"
}

println sum