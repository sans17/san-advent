package us.ligusan.advent2022.d13

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def readNode(s, p, node) {
//    println "s=$s, p=$p, node=$node"
    if(s.size() == p) {
//        println "s=$s, node=$node"
        return node
    }
    
    c = s.charAt(p)
//    println "c=$c"
    switch(c) {
        case '[':
            if(node == null) node = [[], null]
            else {
                node[-1] = []
                node << null
            }
//            println "s=$s, p=$p, node=$node"
            break
        
        case ',':
            node[-2] << node[-1]
            node[-1] = null
            break
        
        case ']':
            if(node[-1] != null) node[-2] << node[-1]
            node.removeLast()
            break
        
        default:
            n = c - (int)'0'
            node[-1] = node[-1] == null ? n : node[-1] * 10 + n
//            println "s=$s, p=$p, node=$node"
    }
    
    return readNode(s, p+1, node)
}

def compare(l, r) {
//    println "l=$l, r=$r"
    if(l instanceof Number && r instanceof Number) {
        ret = l - r
        println "l=$l, r=$r, ret=$ret"
        return ret
    }
    
    lListFlag = l instanceof List
    if(lListFlag && (r instanceof List)) {
        final lSize = l.size()
        final rSize = r.size()
        for(int i=0; i<lSize; i++) {
            if(i>=rSize) break
            else {
                ret = compare(l[i], r[i])
                if(ret != 0) {
                    println "l=$l, r=$r, i=$i, ret=$ret"
                    return ret
                }
            }
        }
        ret = lSize - rSize
        println "l=$l, r=$r, lSize=$lSize, rSize=$rSize, ret=$ret"
        return ret
    }
    else {
        ret = lListFlag ? compare(l, [r]) : compare([l], r)
        println "l=$l, r=$r, ret=$ret"
        return ret
    }
}

//c = compare([7,6,8,9,6], [7,6,8,9])
//println c

sum = 0
i = 0
list.collate(3).each { lines ->
    i++
    
    p1=readNode(lines[0], 0, null)[0]
    p2=readNode(lines[1], 0, null)[0]
//    println lines[0]
    println p1
//    println p1.toString().replaceAll(' ', '')
//    println lines[1]
    println p2
//    println p2.toString().replaceAll(' ', '')
    
    c = compare(p1, p2)
    
    if(c<0) sum += i
    println "i=$i, c=$c, sum=$sum"
    println ''
}

println sum

//pairs.each { compare(it[0], it[1]) }

//inds = pairs.findIndexValues { compare(it[0], it[1]) }

//println "${inds.collect { it + 1 }.sum()}"