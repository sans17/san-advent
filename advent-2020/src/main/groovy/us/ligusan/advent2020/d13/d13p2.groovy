package us.ligusan.advent2020.d13;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def matcher = list[1] =~ /(x|\d+)(,|$)/
matcher.find()

HashMap<Integer, Integer> busMinuteMap = [:]
matcher.eachWithIndex { match, index ->
    if(match[1] != 'x') busMinuteMap[Integer.parseInt(match[1])] = index
}

println busMinuteMap

BigInteger base = 0
BigInteger change = 1
busMinuteMap.each {
    println " 0: ${it} ${base} ${change}"

    //    if(index > 3) return

    for(int i = 1; ; i++) {
        def mod = (base + change * i + it.value).mod(it.key)
        //            println "10: ${i} ${mod}"
        if (mod == 0) {
            base += change * i
            change *= it.key
            break
        }
    }
}

println base
