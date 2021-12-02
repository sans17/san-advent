package us.ligusan.advent2020.d16;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

boolean rules = true
Map<String, Tuple> ranges = [:]

boolean other = false

boolean yourTicket = false
List<Integer> yourList

Map<Integer, Set<String>> availableRules = [:]


list.eachWithIndex { line, lineIndex ->
    //    println " 0: ${lineIndex} ${line} ${rules} ${other} ${yourTicket}"

    if(line.isBlank()) {
        if(rules) rules = false
        if(yourTicket) yourTicket = false
    }

    if(rules) {
        def matcher = line =~ /^([^:]+): (\d+)-(\d+) or (\d+)-(\d+)$/
        matcher.find()

        //        println matcher[0]

        ranges[matcher[0][1]] = [
            Integer.parseInt(matcher[0][2]),
            Integer.parseInt(matcher[0][3]),
            Integer.parseInt(matcher[0][4]),
            Integer.parseInt(matcher[0][5])
        ]
    }

    if(yourTicket || other) {
        def matcher = line =~ /(\d+)(,|$)/
        matcher.find()

        List<Integer> numbersList = matcher.collect {
            Integer.parseInt(it[1])
        }

        if(yourTicket) {
            yourList = numbersList

            yourList.eachWithIndex { number, index ->
                availableRules[index] = new HashSet<String>(ranges.keySet())
            }
        }

        boolean rangeFound = false
        for(number in numbersList) {
            for(rangeValue in ranges.values()) if(rangeFound = (number >= rangeValue[0] && number <= rangeValue[1] || number >= rangeValue[2] && number <= rangeValue[3])) {
                //            println range
                break
            }
            if(!rangeFound) break
        }

        if(rangeFound) {
            //            println numbersList

            for(int i = 0; i < numbersList.size(); i++) {
                for(range in ranges) if(!(numbersList[i] >= range.value[0] && numbersList[i] <= range.value[1] || numbersList[i] >= range.value[2] && numbersList[i] <= range.value[3])) {
                    //                    println "10: ${range}"
                    availableRules[i].remove(range.key)
                }
            }
        }
    }

    if(!yourTicket && line == 'your ticket:') yourTicket = true
    if(!other && line == 'nearby tickets:') other = true
}

Map<String, Integer> fieldsMap = [:]

List<String> removed = []
(availableRules.sort { it.value.size() }).each {
    def fields = it.value as List
    //    println "${fields} ${removed}"

    fields.removeAll(removed)
    if(fields.size() == 1) {
        removed << fields[0]
        fieldsMap[fields[0]] = it.key
    }
    else {
        println "no!!! ${fields}"
    }
}

long res = 1
fieldsMap.each {
    if(it.key.startsWith('departure')) {
        res *= yourList[it.value]

        //        println "${it} ${res}"
    }
}

println res

