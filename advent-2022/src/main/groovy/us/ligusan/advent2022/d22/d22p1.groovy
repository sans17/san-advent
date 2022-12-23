package us.ligusan.advent2022.d22

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

xSize = list.size()
println "xSize=$xSize"

m=[]
instr = ''

part2 = false
list.each { line ->
//    println "line=$line"
    if(part2) instr = line
    else if(line.empty) part2 = true
    else m << Arrays.asList((' ' + line + ' ').toCharArray())
}

ySize = m.collect { it.size() }.max() + 1
println "ySize=$ySize"

wall = (1..ySize).collect { ' ' }

m = m.collect { it += (it.size()..(ySize-1)).collect { ' ' } }
m.push(wall)
m << wall


println ''
m.each { println it }


println ''
println instr
println ''

p = [1, m[1].findIndexOf { it == '.' }, [0, 1] ]
println "p=$p"

def walk(l) {
    for(j=0; j<l; j++) {
        np = p[0..1]
        inner: for(;;) {
            np = [np, p[2]].transpose()*.sum()
            if(np[0]<0) np[0]=xSize-1
            np[0] = np[0]%xSize 
            if(np[1]<0) np[1] = ySize-1
            np[1] = np[1]%ySize
//            println "np=$np"
            switch(m[np[0]][np[1]]) {
                case '#':
                    return
    
                case '.':
                    p[0] = np[0]
                    p[1] = np[1]
                    break inner
            }
        }
    }
}

l = 0
for(i = 0; i<instr.size(); i++) {
    tm = null

    c = instr[i]
//    println "i=$i, c=$c"
    switch(c) {
        case 'R':
            tm = [[0, 1], [-1, 0]]
            break

        case 'L':
            tm = [[0, -1], [1, 0]]
            break
            
        default:
            l = l*10 + ((int)c - (int)'0')
//            println "l=$l"
            continue
    }

    println "i=$i, l=$l, c=$c"
    walk(l)
//    println "p=$p"
    p[2] = tm.collect { [it, p[2]].transpose().collect { it.inject(1) { res, i -> res*i } }.sum() }
    l = 0
    
    println "p=$p"
}

walk(l)
println "l=$l, p=$p"

dirs=[[0,1], [1,0], [0,-1], [-1,0]]

count = 1_000 * p[0] + 4 * p[1] + dirs.indexOf(p[2])
println count