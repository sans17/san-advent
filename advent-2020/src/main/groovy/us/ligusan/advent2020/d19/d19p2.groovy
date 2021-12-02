package us.ligusan.advent2020.d19;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {
    it
}

boolean rulesFlag = true
Map<Integer, String> rules = [:]

Map<Integer, String> regexMap = [:]
int num = 0

list.eachWithIndex { line, lineIndex ->
    println "${lineIndex} ${line}"

    if(!rulesFlag){
        def matcher = line =~ /^${regexMap[0]}$/
        if(matcher.find()) num++
    }

    if(rulesFlag && line.isBlank()) {
        rulesFlag = false

        for(int rulNum in [31, 42, 0]) {
            String regex = "(${rules[rulNum]})"
            for(def matcher ; true; ) {
                matcher = regex =~ /\((\d+) (\d+) \| (\d+) (\d+)\)/
                if(matcher.find()) {
                    regex = matcher.replaceFirst("((${matcher[0][1]})(${matcher[0][2]})|(${matcher[0][3]})(${matcher[0][4]}))")

                    continue
                }

                matcher = regex =~ /\((\d+) \| (\d+)\)/
                if(matcher.find()) {
                    regex = matcher.replaceFirst("((${matcher[0][1]})|(${matcher[0][2]}))")

                    continue
                }

                matcher = regex =~ /\((\d+) (\d+)\)/
                if(matcher.find()) {
                    regex = matcher.replaceFirst("(${matcher[0][1]})(${matcher[0][2]})")

                    continue
                }

                matcher = regex =~ /\((\d+)\)/
                if(matcher.find()) {
                    int i = Integer.parseInt(matcher[0][1])

                    if(i == 8) {
                        regex = matcher.replaceFirst("${regexMap[42]}+")

                        println "8 -> ${rulNum} ${regex}"
                    }
                    else if(i == 11) regex = matcher.replaceFirst("(${regexMap[42]}${regexMap[31]}|${regexMap[42]}${regexMap[42]}${regexMap[31]}${regexMap[31]}|${regexMap[42]}${regexMap[42]}${regexMap[42]}${regexMap[31]}${regexMap[31]}${regexMap[31]}|${regexMap[42]}${regexMap[42]}${regexMap[42]}${regexMap[42]}${regexMap[31]}${regexMap[31]}${regexMap[31]}${regexMap[31]})")
                    else regex = matcher.replaceFirst("(${rules[i]})")

                    //                    println "9 -> ${rulNum} ${regex}"

                    continue
                } else break
            }
            for( ; (matcher = regex =~ /"([ab])"/).find() ; regex = matcher.replaceFirst("${matcher[0][1]}")) ;
                for( ; (matcher = regex =~ /\(([ab]+)\)/).find() ; regex = matcher.replaceFirst("${matcher[0][1]}")) ;

                println "10 -> ${rulNum} ${regex}"

            regexMap[rulNum] = regex
            //            regexMap.each { key, value ->
            //                println "11 -> ${key} ${value}"
            //            }
        }
    }

    if(rulesFlag) {
        def matcher = line =~ /^(\d+): (.*)$/
        matcher.find()

        //        println matcher[0]

        rules.put(Integer.parseInt(matcher[0][1]), matcher[0][2])
    }
}

println num