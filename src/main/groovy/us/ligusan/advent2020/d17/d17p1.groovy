package us.ligusan.advent2020.d17;

def nextActive = [] as Set
(new File(getClass().getResource('input.txt').toURI()).collect { it }).withIndex().each { line, lineIndex ->
    Arrays.asList(line.toCharArray()).withIndex().each { activeChar, charIndex ->
        if(activeChar == '#') nextActive << [charIndex, lineIndex, 0]
    }
}

(1..6).each {
    println "${nextActive.size()} ${nextActive}"

    def active = nextActive
    nextActive = [] as Set

    def processed = [] as Set
    active.each { element ->
        for(int i1 in -1..1) for(int j1 in -1..1) for(int k1 in -1..1) {
            int x1 = element[0] + i1
            int y1 = element[1] + j1
            int z1 = element[2] + k1

            def check = [x1, y1, z1]
            if(processed.add(check)) {
                int nnum = 0
                for(int i2 in -1..1) for(int j2 in -1..1) for(int k2 in -1..1) if((i2 != 0 || j2 != 0 || k2 != 0) && active.contains([x1 + i2, y1 + j2, z1 + k2])) nnum++

                println "0: ${check} ${active.contains(check)} ${nnum}"

                if(nnum == 3 || nnum == 2 && active.contains(check)) nextActive << check
            }
        }
    }

    println "${it} ${nextActive.size()}"
}

