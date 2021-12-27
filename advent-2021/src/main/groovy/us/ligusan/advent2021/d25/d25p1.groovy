package us.ligusan.advent2021.d25

import java.lang.invoke.SwitchPoint

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

int ySize = list.size()
int xSize = list[0].size()
println "xSize=${xSize}, ySize=${ySize}"

def m = [:]
list.withIndex().each { line, y ->
    line.eachWithIndex() { c, x ->
        if(['v', '>'].contains(c)) m[[x,y]] = c
    }
}
println "m.size=${m.size()}, m=${m}"

int count = 1
for(;; count++) {
    def e = m.findAll { key, value ->
        value == '>' && !m.containsKey([(key[0] +1)%xSize, key[1]])
    }
    int num = e.size()
    m.keySet().removeAll(e.keySet())
//    println "num=${num}, e=${e}"
    
    m += e.collectEntries { key, value ->
        [[(key[0] + 1)%xSize, key[1]], '>']        
    }

    e = m.findAll { key, value ->
        value == 'v' && !m.containsKey([key[0], (key[1] +1)%ySize])
    }
    num += e.size()
    m.keySet().removeAll(e.keySet())
    
    m += e.collectEntries { key, value ->
        [[key[0], (key[1] +1)%ySize], 'v']        
    }

//    println "num=${num}, m.size=${m.size()}"    
    if(num == 0) break
}
println count