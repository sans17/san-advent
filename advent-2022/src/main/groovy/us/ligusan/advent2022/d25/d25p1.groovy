package us.ligusan.advent2022.d25

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

bi5 = new BigInteger('5')

nums = list.collect { line ->
    size = line.size()
    ret = (size-1..0).collect {
//        println "it=$it, line[$it]=${line[it]}"
        d = 0
        switch(line[it]) {
            case '0':
                d = 0
                break

            case '1':
                d = 1
                break
                
            case '2':
                d = 2
                break
                
            case '-':
                d = -1
                break
                
            case '=':
                d = -2
                break
        }
        bi5.pow(size-1 - it)*d
    }.sum()
    println "line=$line, ret=$ret"
    ret
}

num = nums.sum()
println "num=$num"

ds=[]
for(i=1; ;i++) {
//    println "i=$i"
    r = num.remainder(bi5)
//    println "r=$r"
    switch(r) {
        case 0:
            ds.push('0')
            break
            
        case 1:
            ds.push('1')
            break

        case 2:
            ds.push('2')
            break

        case 3:
            ds.push('=')
            break

        case 4:
            ds.push('-')
            break
    }

    if(num + 2 < 5) break
    
    num = (num+2).divide(bi5)
//    println "num=$num"
}

println ds.join()