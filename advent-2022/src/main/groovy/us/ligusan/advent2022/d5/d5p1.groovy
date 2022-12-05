package us.ligusan.advent2022.d5

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

s = []
t = []
r = []

first = true
list.each { line ->
    println "line={${line}}"
    if(!first) {
        split = (line =~ /move (\d+) from (\d+) to (\d+)/)[0]
        
        r.add(split[1..3].collect { Integer.valueOf(it) } )
    }

    if (line.isBlank()) {
        first = false;

        t = s.transpose().collect { 
            it.removeAll(' ');
            return it
        }
    }
    
    if(first) {
        a = Arrays.asList(line.toCharArray()).collate(4)
        if(a[0][1] != '1') {
            s.add(0, a.collect { it[1] })
        }
    }
}

println t
println r

r.each { c ->
    println c
    
    s = t[c[1]-1].size()
    
    p = t[c[1]-1].subList(s-c[0],s)
    println p
    
    
    t[c[1]-1] = t[c[1]-1].subList(0, s-c[0])
    
    t[c[2]-1] += p.reverse()
    
    println t
}

o = t.collect { it[-1] }.join()

println o