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

println coords.count { it.value % 2 == 1}
