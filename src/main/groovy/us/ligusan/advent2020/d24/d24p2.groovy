package us.ligusan.advent2020.d24;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {
    it
}

def coords = [:]

list.each { line ->
    println line
    
    def xy = [0, 0]
    for(String sub = line; !sub.isBlank();) {
        int prefixLength = 2
        if(sub.startsWith('e')) {
            prefixLength = 1
            xy = xy.collect { it + 1 }
        } else if(sub.startsWith('se')) xy[0]++
        else if(sub.startsWith('sw')) xy[1]--
        else if(sub.startsWith('w')) {
            prefixLength = 1
            xy = xy.collect { it - 1 }
        } else if(sub.startsWith('nw')) xy[0]--
        else xy[1]++
        
        sub = sub.substring(prefixLength)
    }
    
    coords.compute(xy, (key, value) -> (value?:0) + 1)
    
//    println coords
}

println coords

def nextActive = coords.findResults { it.value % 2 == 1 ? it.key : null }

def cube = ((1..2).collect { -1..1 }).combinations()

(1..100).each {
//    println "${nextActive.size()} ${nextActive}"

    def active = nextActive

    def processed = [] as Set
    nextActive = (active.collectMany { element ->
        cube.findResults { i ->
            def check = [element, i].transpose()*.sum()
            if(processed.add(check)) {
                int nnum = cube.sum { j ->
                    !(j.every { it == 0 } || (j[0] * j[1] == -1)) && active.contains([check, j].transpose()*.sum()) ? 1 : 0
                }

//                println "0: ${check} ${active.contains(check)} ${nnum}"

                if(nnum == 2 || nnum == 1 && active.contains(check)) return check
            }
            return null
        }
    }) as Set

    println "${it} ${nextActive.size()}"
}

