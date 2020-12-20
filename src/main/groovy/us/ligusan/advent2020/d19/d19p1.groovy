package us.ligusan.advent2020.d19;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {
    it
}

boolean rulesFlag = true
Map<Integer, String> rules = [:]

String regex
int num = 0

list.eachWithIndex { line, lineIndex ->
    println "${lineIndex} ${line}"

    if(!rulesFlag){
        def matcher = line =~ /^${regex}$/
        if(matcher.find()) num++
    }

    if(rulesFlag && line.isBlank()) {
        rulesFlag = false

        regex = "(${rules[0]})"
        for(def matcher ; true; ) {
            matcher = regex =~ /\((\d+) (\d+) \| (\d+) (\d+)\)/
            if(matcher.find()) {
                def i = matcher[0][1..4].collect {
                    Integer.parseInt(it)
                }
                //                println " 0 -> ${regex}"

                regex = matcher.replaceFirst("((${rules[i[0]]})(${rules[i[1]]})|(${rules[i[2]]})(${rules[i[3]]}))")

                continue
            }

            matcher = regex =~ /\((\d+) \| (\d+)\)/
            if(matcher.find()) {
                def i = matcher[0][1..2].collect {
                    Integer.parseInt(it)
                }
                //                println " 1 -> ${regex}"

                regex = matcher.replaceFirst("((${rules[i[0]]})|(${rules[i[1]]}))")

                continue
            }

            matcher = regex =~ /\((\d+) (\d+)\)/
            if(matcher.find()) {
                def i = matcher[0][1..2].collect {
                    Integer.parseInt(it)
                }
                //                println " 2 -> ${regex}"

                regex = matcher.replaceFirst("(${rules[i[0]]})(${rules[i[1]]})")

                continue
            }

            matcher = regex =~ /\((\d+)\)/
            if(matcher.find()) {
                regex = matcher.replaceFirst("(${rules[Integer.parseInt(matcher[0][1])]})")

                continue
            } else break
        }
        for( ; (matcher = regex =~ /"([ab])"/).find() ; regex = matcher.replaceFirst("${matcher[0][1]}")) ;
            //            println "9 -> ${regex}"

            for( ; (matcher = regex =~ /\(([ab]+)\)/).find() ; regex = matcher.replaceFirst("${matcher[0][1]}")) ;

            println "10 -> ${regex}"
    }

    if(rulesFlag) {
        def matcher = line =~ /^(\d+): (.*)$/
        matcher.find()

        //        println matcher[0]

        rules.put(Integer.parseInt(matcher[0][1]), matcher[0][2])
    }
}

println num