package us.ligusan.advent2020.d17;

def nextActive = [] as Set
(new File(getClass().getResource('input.txt').toURI()).collect { it }).withIndex().each { line, lineIndex ->
    Arrays.asList(line.toCharArray()).withIndex().each { activeChar, charIndex ->
        if(activeChar == '#') nextActive << [charIndex, lineIndex, 0, 0]
    }
}

def ball = [-1..1, -1..1, -1..1, -1..1].combinations()

(1..6).each {
    println "${nextActive.size()} ${nextActive}"

    def active = nextActive
    nextActive = [] as Set

    def processed = [] as Set
    active.each { element ->
        ball.each { i ->
            def check = [element, i].transpose()*.sum()
            if(processed.add(check)) {
                int nnum = 0
                ball.combinations().each { j ->
                    if(!(j.every { it == 0 }) && active.contains([check, j].transpose()*.sum())) nnum++
                }

                //                println "0: ${check} ${active.contains(check)} ${nnum}"

                if(nnum == 3 || nnum == 2 && active.contains(check)) nextActive << check
            }
        }
    }

    println "${it} ${nextActive.size()}"
}
