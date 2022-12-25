package us.ligusan.advent2022.d24

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

xSize = list.size()
ySize = list[0].size()
println"xSize=$xSize, ySize=$ySize"

bs = list[1..xSize-2].collect {
    Arrays.asList(it.toCharArray())[1..ySize-2].collect {
        switch(it) {
            case 'v':
                return 1
    
            case '>':
                return 2
    
            case '^':
                return 4
    
            case '<':
                return 8
                
            default:
                return 0
        }
    }
}
println "bs=$bs"

p = [-1, list[0].indexOf('.')-1]
sp = p
println "p=$p"

ep0 = [xSize-2, list[xSize-1].indexOf('.')-1]
println "ep0=$ep0"

cube = (1..2).collect { -1..1 }.combinations().findAll { it.any { it == 0} }
println "cube=$cube"

def dist(p1, p2) {
    [p1, -p2].transpose()*.sum().collect { it.abs() }.sum()
}

t=[]
for(j=0; j<3; j++) {
    ep = j%2 == 0 ? ep0 : sp
    md = dist(p, ep)
    println ''
    println "j=$j, ep=$ep, md=$md"

    pbs = [[p, bs]]
    pts = [] as Set
    println "p=$p, bs=$bs"

    outer: for(i=0; ; i++) {
      println "i=$i, pbs.size=${pbs.size()}"
      
      npbs = []
      for(pb in pbs) {
          if(pts.contains(pb)) continue
          pts << pb
          
        //  println "pb=$pb"
          d = dist(pb[0], ep)
          if(d<md) {
              md = d
              println "pb[0]=${pb[0]}, d=$d"
          }
          
    //      println pb[1]
    //      println ''
          nbs = pb[1].withIndex().collect { line, x ->
              line.withIndex().collect { n, y ->
    //              println "x=$x, y=$y"
    //              println "pb[1][${x-1}][$y]=${pb[1][x-1][y]}"
    //              println "pb[1][$x][${y-1}]=${pb[1][x][y-1]}"
    //              println "pb[1][${x+1}][$y]=${pb[1][(x+1)%(xSize-2)][y]}"
    //              println "pb[1][$x][${y+1}]=${pb[1][x][(y+1)%(ySize-2)]}"
                  ret = (pb[1][x-1][y] & 1) + (pb[1][x][y-1] & 2) + (pb[1][(x+1)%(xSize-2)][y] & 4) + (pb[1][x][(y+1)%(ySize-2)] & 8)
    //              println "ret=$ret"
                  ret
              }
          }
    //      println "nbs=$nbs"
          
          for(c in cube) {
              np = [pb[0], c].transpose()*.sum()
              if(np != sp && np != ep0) {
                  if(np[0]<0 || np[0]>=xSize-2) continue
                  if(np[1]<0 || np[1]>=ySize-2) continue
                  if(nbs[np[0]][np[1]] != 0) continue
              }
              
              if(np == ep) {
                  t << i+1
                  p = ep
                  bs = nbs
                  
                  break outer
              }
    
    //          println "np=$np"              
              npbs << [np, nbs]
          }
        
          npbs.sort { dist(it[0], ep) }
          pbs = npbs
      }
    }
}

println ''
println "t=$t"
println t.sum()
