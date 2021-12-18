package us.ligusan.advent2021.d17

def list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def split = (list[0] =~ /target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)/)[0]
//println split

def box = split[1..-1].collect { Integer.valueOf(it) }
println box

int maxH = 0;
for(int xv = 1; xv <= box[1]; xv++)
    for(int yv = 1; yv <= -box[2]; yv++) {
        println "xv=${xv}, yv=${yv}"

        def p = [0, 0]
        def v = [xv, yv]
        def h = 0
        boolean inTheBox = false
        for(int t = 0; !inTheBox && p[0] <= box[1] && p[1] >= box[2]; t++){
//            println "t=${t}, p=${p}, v=${v}"
            p = [p, v].transpose()*.sum()
            v = [ v[0] > 0 ? v[0] -1 : 0, v[1] -1 ]
            h = Math.max(h, p[1])

            inTheBox = p[0] >= box[0] && p[0] <= box[1] && p[1] >= box[2] && p[1] <= box[3]
            
//            println "h=${h}, inTheBox=${inTheBox}"
        }
        if(inTheBox) maxH = Math.max(maxH, h)
    }
println "maxH=${maxH}"
