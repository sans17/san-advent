package us.ligusan.advent2022.d10

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

x=1
c=0

s=(1..6).collect { [] }

def draw() {
    println "c=$c, ${(int)((c-1)/40)} ${(c-1)%40}"
    s[(int)((c-1)/40)] << ([x-1, x, x+1].contains((c-1)%40) ? '#' : '.')
}

list.each { line ->
    split = (line =~ /(noop|addx (-?\d+))/)[0]
//    println "split=$split"
    
    if(split[1] == 'noop') {
        c++
        draw()
    } else {
        c++
        draw()
        c++
        draw()
        
        x+=Integer.valueOf(split[2])
    }
    
//    println "c=$c, x=$x"
}

s.each { println it.join() }