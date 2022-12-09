package us.ligusan.advent2022.d9

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

len = 9
h = (0..len).collect { [0, 0] }

l = [h[len]] as Set

list.each { line ->
    split = (line =~ /(R|L|U|D) (\d+)/)[0]
    println "split=$split"
    
    hd = [0, 0]
    switch (split[1]) {
        case 'R':
            hd[0] = 1;
            break;
            
        case 'L':
            hd[0] = -1;
            break;
            
        case 'U':
            hd[1] = 1;
            break;
            
        case 'D':
            hd[1] = -1;
            break;
    }
    println "hd=$hd"
    
    (1..Integer.valueOf(split[2])).each {
        h[0] = [h[0], hd].transpose()*.sum()
        
        (1..len).each { j ->
            htt=[h[j-1], h[j]].transpose().collect { it[0] - it[1] };
            td = htt.find { it.abs() > 1 } ? htt.collect { Integer.signum(it) } : htt.collect { it.abs() > 1 ? Integer.signum(it) : 0 }
        
            h[j] = [h[j], td].transpose()*.sum()
        }
        println "h=$h"
        l << h[len]
    }
    
    println "l=$l"
}

println l.size()