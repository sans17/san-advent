package us.ligusan.advent2022.d7

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

lsFlag = false
processed = [] as Set
sizeMap = [:]

cd = '/'
list.each { line ->
//    println "line=$line"
    
    if (line[0] == '$') {
        if (line == '$ ls') {
            lsFlag = true
        } else {
            if (lsFlag) {
                lsFlag = false
                processed += cd
                
//                println "processed=$processed"
            }
            
            split = (line =~ /\$ cd (.+)/)[0]
//            println "split=$split"
            
            switch (split[1]) {
                case '/':
                    cd = '/'
                    break
                    
                case '..':
                    cd = cd.replaceFirst('[^/]+/$', '');
                    break
                    
                default:
                    cd = cd + split[1] + '/'
            }
//            println "cd=$cd"

            sizeMap.putIfAbsent(cd, 0)

//            println "sizeMap=$sizeMap"
        }
    } else if (!processed.contains(cd)) {
        split = (line =~ /(dir|\d+) (.+)/)[0]
//        println "split=$split"
        
        if ('dir' != split[1]) {
            sizeMap.each { entry ->
                if (cd.startsWith(entry.key)) {
                    entry.value = entry.value + new BigInteger(split[1])
                    
//                    println "entry=$entry"
                }
            }
        }
    }
}

println "sizeMap=$sizeMap"

sizeToDelete = 30_000_000 - (70_000_000 - sizeMap['/'])

sizeMap.removeAll { it.value < sizeToDelete }

println "sizeMap=$sizeMap"

println (sizeMap.sort { it.value }.values()[0])