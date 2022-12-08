package us.ligusan.advent2022.d8

list = new File(getClass().getResource('input.txt').toURI()).collect { Arrays.asList(it.toCharArray()).collect { (int)it - (int)'0' } }

xSize = list.size()
ySize = list[0].size()

max = 0
(1..xSize-2).each { x ->
    (1..ySize-2).each { y ->
        s = 1

//        println "list[$x][$y]=${list[x][y]}"
        ls = 0
        for(int i = y-1; i >= 0; i--) {
//            println "list[$x][$i]=${list[x][i]} ls=$ls"
            if(list[x][i] >= list[x][y]) {
                ls = y - i
                break
            }
        }
        if(ls==0) ls=y
        s *= ls
//        println "ls=$ls"
        
        ls = 0
        for(int i = y+1; i < ySize; i++) if(list[x][i] >= list[x][y]) {
            ls = i - y
            break
        }
        if(ls==0) ls=ySize-1 - y
        s *= ls
//        println "ls=$ls"
        
        ls = 0
        for(int i = x-1; i >= 0; i--) if(list[i][y] >= list[x][y]) {
            ls = x - i
            break
        }
        if(ls==0) ls=x
        s *= ls
//        println "ls=$ls"
        
        ls = 0
        for(int i = x+1; i < xSize; i++) if(list[i][y] >= list[x][y]) {
            ls = i - x
            break
        }
        if(ls==0) ls=xSize-1 - x
        s *= ls
//        println "ls=$ls"
        
        print "list[$x][$y]=${list[x][y]} s=$s "
//        println "s=$s "
        
        if(s>max) max=s
    }
    println ''
}
println max
