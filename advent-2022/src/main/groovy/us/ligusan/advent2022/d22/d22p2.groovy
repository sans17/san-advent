package us.ligusan.advent2022.d22

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

xSize = list.size()
println "xSize=$xSize"

dirs = [['>', [0,1]], ['v', [1,0]], ['<', [0,-1]], ['^',[-1,0]]]
tpms = [R:[[0, 1], [-1, 0]], L:[[0, -1], [1, 0]], B:[[-1, 0], [0, -1]], F:[[1, 0], [0, 1]]]

em = [
    [[0, 51], [0, 100], '^', 'R', [151, 1]],
    [[0, 101], [0, 150], '^', 'F', [200, 1]],
    [[1, 50], [50, 50], '<', 'B', [150, 1]],
    [[51, 50], [100, 50], '<', 'L', [101, 1]],
    [[1, 151], [50, 151], '>', 'B', [150, 100]],
    [[51, 101], [51, 150], 'v', 'R', [51, 100]],
    [[151, 51], [151, 100], 'v', 'R', [151, 50]],
    [[151, 0], [200, 0], '<', 'L', [1, 51]],
    [[201, 1], [201, 50], 'v', 'F', [1, 101]],
    [[101, 0], [150, 0], '<', 'B', [50, 51]],
    [[100, 1], [100, 50], '^', 'R', [51, 51]],
    [[101, 101], [150, 101], '>', 'B', [50, 150]],
    [[51, 101], [100, 101], '>', 'L', [50, 101]],
    [[151, 51], [200, 51], '>', 'L', [150, 51]]
    ]

m=[]
instr = ''

part2 = false
list.each { line ->
//    println "line=$line"
    if(part2) instr = line
    else if(line.empty) part2 = true
    else m << Arrays.asList((' ' + line + ' ').toCharArray())
}

ySize = m.collect { it.size() }.max()
println "ySize=$ySize"

neis = m.withIndex().findAll { line, ind -> line[1] != ' ' }.collect { it[1] }

wall = (0..ySize).collect { ' ' }

m = m.collect { it += (it.size()..ySize).collect { ' ' } }
m.push(wall)
m << wall


println ''
m.each { println it }


println ''
println instr
println ''

p = [[1, m[1].findIndexOf { it == '.' }], '>' ]
println "p=$p"

def getRot(p) {
    println "p=$p"
    ed = em.find { 
        hf = it[0][0] == it[1][0]
        
        mi = hf ? it[0][1] : it[0][0]
        ma = hf ? it[1][1] : it[1][0]
        b = hf ? p[0][1] : p[0][0]
        println "hf=$hf, mi=$mi, ma=$ma, b=$b"
        
        (hf ? p[0][0] == it[0][0] : p[0][1] == it[0][1]) && mi <= b && b <= ma &&  p[1] == it[2]
    }
    ret = [ed[3], diff(ed[0], ed[3], ed[4])]
    println "ret=$ret"
    ret
}

def diff(a, t, b) {
    [b, -turnPoint(a, t)].transpose()*.sum()
}

def turnPoint(ptt, td) {
    println "ptt=$ptt, td=$td"
    ret = tpms[td].collect { [it, ptt].transpose().collect { it.inject(1) { res, i -> res*i } }.sum() }
    println "ret=$ret"
    ret
}

def turnDirection(cd, td) {
    dirs.find { it[1] == turnPoint(dirs.find { it[0] == cd }[1], td) }[0]
}


def walk(l) {
    for(j=0; j<l; j++) {
        inner: for(;;) {
            np = [[p[0], dirs.find { it[0] == p[1] }[1]].transpose()*.sum(), p[1]]
            println "np=$np"

            if(m[np[0][0]][np[0][1]] == ' ') {
                rot = getRot(np)
                rc = rot[0]
                av = rot[1]
                println "rc=$rc, av=$av"
                
                np = [[turnPoint(np[0], rc), av].transpose()*.sum(), turnDirection(np[1], rc)]
                println "np=$np"
            }

            if(m[np[0][0]][np[0][1]] == '#') return
            else {
                p=np
                break inner
            }
        }
    }
}

l = 0
for(i = 0; i<instr.size(); i++) {
    tpm = null

    c = instr[i]
//    println "i=$i, c=$c"
    switch(c) {
        case 'R':
        case 'L':
            break
            
        default:
            l = l*10 + ((int)c - (int)'0')
//            println "l=$l"
            continue
    }

    println "i=$i, l=$l, c=$c"
    walk(l)
    println "p=$p"
    p[1] = turnDirection(p[1], c)
    l = 0
    
    println "p=$p"
}

println "l=$l"
walk(l)
println "p=$p"


count = 1_000 * p[0][0] + 4 * p[0][1] + dirs.findIndexOf { it[0] == p[1] }
println count