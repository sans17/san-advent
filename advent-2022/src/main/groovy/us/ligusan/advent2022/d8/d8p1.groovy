package us.ligusan.advent2022.d8

list = new File(getClass().getResource('input.txt').toURI()).collect { Arrays.asList(it.toCharArray()).collect { (int)it - (int)'0' } }

xSize = list.size()
ySize = list[0].size()

count = 0
(1..xSize-2).each { x ->
    (1..ySize-2).each { y ->
        
        v = (list[x][0..y-1].max() < list[x][y]) || (list[x][y+1..-1].max() < list[x][y]) || ((0..x-1).collect { list[it][y] }.max() < list[x][y]) || ((x+1..xSize-1).collect { list[it][y] }.max() < list[x][y])
        print "list[$x][$y]=${list[x][y]} v=$v   "
        
        if(v) count++
    }
    println ""
}
println count

println count + 2*xSize + 2*ySize - 4
