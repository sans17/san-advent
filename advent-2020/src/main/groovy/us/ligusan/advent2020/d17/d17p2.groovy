package us.ligusan.advent2020.d17;

def nextActive = ((new File(getClass().getResource('input.txt').toURI()).collect { it }).withIndex().collectMany { line, lineIndex ->
    Arrays.asList(line.toCharArray()).withIndex().findResults { activeChar, charIndex ->
        activeChar == '#' ? [charIndex, lineIndex, 0, 0] : null
    }
}) as Set

def cube = ((1..4).collect { -1..1 }).combinations()

(1..6).each {
    println "${nextActive.size()} ${nextActive}"

    def active = nextActive

    def processed = [] as Set
    nextActive = (active.collectMany { element ->
        cube.findResults { i ->
            def check = [element, i].transpose()*.sum()
            if(processed.add(check)) {
                int nnum = cube.sum { j ->
                    !(j.every { it == 0 }) && active.contains([check, j].transpose()*.sum()) ? 1 : 0
                }

                //                println "0: ${check} ${active.contains(check)} ${nnum}"

                if(nnum == 3 || nnum == 2 && active.contains(check)) return check
            }
            return null
        }
    }) as Set

    println "${it} ${nextActive.size()}"
}
