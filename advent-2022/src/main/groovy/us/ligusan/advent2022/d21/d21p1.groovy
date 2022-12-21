package us.ligusan.advent2022.d21

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

println "list=$list"

n = -1
outer: for(j=0; ; j++) {
    println "j=$j"
    for(i=0; i<list.size(); i++) {
        split = (list[i] =~ /(\S+): (.+)/)[0]
//        println "split=$split"
        
        val = split[2]
        if(val ==~ /\d+ [-+*\/] \d+/) {
            val_split = (val =~ /(\d+) ([-+*\/]) (\d+)/)[0]
            l = new BigInteger(val_split[1])
            r = new BigInteger(val_split[3])
            switch(val_split[2]) {
                case '-':
                    val = l-r
                    break
                case '+':
                    val = l+r
                    break
                case '*':
                    val = l*r
                    break
                case '/':
                    val = l/r
                    break
            }
            val = val.toString()
        }
        println "val=$val"
        
        if(val ==~ /\d+/) {
            if(split[1] == 'root') {
                n = val
                break outer
            }
            
            list.remove(i)

            list = list.collect { it.replaceAll(split[1], val) }
            println "list=$list"
            break
        }
    }
}
println n