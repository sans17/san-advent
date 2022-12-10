package us.ligusan.advent2022.d10

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

x=1
c=0

lm=[:]

def checkLevel() {
    if([20, 60, 100, 140, 180, 220].contains(c)) lm[c]=x
}

list.each { line ->
    split = (line =~ /(noop|addx (-?\d+))/)[0]
    println "split=$split"
    
    if(split[1] == 'noop') {
        c++
        checkLevel()
    } else {
        c++
        checkLevel()
        c++
        checkLevel()
        
        x+=Integer.valueOf(split[2])
    }
    
    println "c=$c, x=$x"
}

println "lm=$lm"

println lm.collect { it.key * it.value }.sum()
