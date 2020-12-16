package us.ligusan.advent2020.d16;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

boolean rules = true
Set<Tuple> ranges = []

boolean other = false

int sum = 0

list.eachWithIndex { line, lineIndex ->
    //    println "${lineIndex} ${line} ${rules} ${other}"

    if(rules && line.isBlank()) rules = false

    if(rules) {
        def matcher = line =~ /^[^:]+: (\d+)-(\d+) or (\d+)-(\d+)$/
        matcher.find()

        //        println matcher[0]

        ranges << [
            Integer.parseInt(matcher[0][1]),
            Integer.parseInt(matcher[0][2]),
            Integer.parseInt(matcher[0][3]),
            Integer.parseInt(matcher[0][4])
        ]
    }

    if(other) {

        def matcher = line =~ /(\d+)(,|$)/
        matcher.find()

        matcher.each {
            otherNumber = Integer.parseInt(it[1])

            println otherNumber

            boolean rangeFound = false
            for(range in ranges) if(rangeFound = (otherNumber >= range[0] && otherNumber <= range[1] || otherNumber >= range[2] && otherNumber <= range[3])) {
                println range
                break
            }

            if(!rangeFound) {
                sum += otherNumber

                println "--- ${sum}"
            }
        }
    }

    if(!other && line == 'nearby tickets:') other = true
}

println sum

