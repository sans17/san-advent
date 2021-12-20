package us.ligusan.advent2021.d18

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def level(int l, list) {
    def ret = list.collectMany { it instanceof List ? level(l+1, it) : [l, it] }
    //    println "l=${l}, list=${list}, ret=${ret}"
    return ret
}

def mag(list) {
    while(list.size() > 1)
        for(int i = 0; i<list.size(); i++) {
            if(list[i][0] == list[i+1][0]) {
                def l = list[i][1]
                def r = list[i+1][1]
                int newL = list[i][0] -1
    //            println "l=${l}, r=${r}"
    
                list.remove(i)
                list.remove(i)
                list.add(i, [newL, 3*l + 2*r])
    //            println s
                break
            }
        }
   return list[0][1]
}

def fishSum(l, r) {
    def ret = l.collect { [it[0] + 1, it[1]] }
    ret += r.collect { [it[0] + 1, it[1]] }
//    println "s=${s}"

    int j = 0;
    loop: for(boolean found=true; found;) {
        found=false
        j++

        for(int i = 0; i<ret.size(); i++)
            if(ret[i][0] == 5) {
                found = true
                if(i > 0) ret[i-1][1] += ret[i][1]
                if(i+2 < ret.size()) ret[i+2][1] += ret[i+1][1]
                ret.remove(i)
                ret.remove(i)
                ret.add(i, [4, 0])

//                println "j=${j}, i=${i}, s=${s}"
                continue loop
            }

        for(int i = 0; i<ret.size(); i++)
            if(ret[i][1] > 9) {
                found = true
                int v = ret[i][1]
                int v1 = v/2
                int newL = ret[i][0] + 1

                ret.remove(i)
                ret.add(i, [newL, v1])
                ret.add(i+1, [newL, v - v1])

//                println "j=${j}, i=${i}, v=${v}, v1=${v1}, newL=${newL}, s=${s}"
                continue loop
            }
    }
    return ret
}

def nums = list.collect {
    level(1, Eval.me(it)).collate(2)
}
nums.each { println it }
println '------------------'

println (([nums, nums].combinations().collect  { mag(fishSum(it[0], it[1])) } ).max())
