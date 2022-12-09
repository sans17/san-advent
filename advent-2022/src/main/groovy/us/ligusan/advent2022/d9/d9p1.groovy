package us.ligusan.advent2022.d9

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

h = [0, 0]
t = [0, 0]

l = [t] as Set

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
    
    for(int i = 1; i <= Integer.valueOf(split[2]); i++) {
        h = [h, hd].transpose()*.sum()
        htt=[h, t].transpose().collect { it[0] - it[1] };
        println "htt=$htt"
        td = htt.find { it.abs() > 1 } ? htt.collect { Integer.signum(it) } : htt.collect { it.abs() > 1 ? Integer.signum(it) : 0 }
        
        t = [t, td].transpose()*.sum()
        println "h=$h, td=$td, t=$t"
        
        l << t
    }
    
    println "l=$l"
}

println l.size()