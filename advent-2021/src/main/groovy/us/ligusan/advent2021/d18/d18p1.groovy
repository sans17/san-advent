package us.ligusan.advent2021.d18

def list = new File(getClass().getResource('input.txt').toURI()).collect { it }

def level(int l, list) {
    def ret = list.collectMany { it instanceof List ? level(l+1, it) : [l, it] }
    //    println "l=${l}, list=${list}, ret=${ret}"
    return ret
}

def nums = list.collect {
    level(1, Eval.me(it)).collate(2)
}
nums.each { println it }
println '------------------'

def s = nums[0]
nums[1..-1].each {
    //    println "s=${s}, it=${it}"

    s = s.collect { [it[0] + 1, it[1]] }
    s += it.collect { [it[0] + 1, it[1]] }
//    println "s=${s}"

    int j = 0;
    loop: for(boolean found=true; found;) {
        found=false
        j++

        for(int i = 0; i<s.size(); i++)
            if(s[i][0] == 5) {
                found = true
                if(i > 0) s[i-1][1] += s[i][1]
                if(i+2 < s.size()) s[i+2][1] += s[i+1][1]
                s.remove(i)
                s.remove(i)
                s.add(i, [4, 0])

//                println "j=${j}, i=${i}, s=${s}"
                continue loop
            }

        for(int i = 0; i<s.size(); i++)
            if(s[i][1] > 9) {
                found = true
                int v = s[i][1]
                int v1 = v/2
                int newL = s[i][0] + 1

                s.remove(i)
                s.add(i, [newL, v1])
                s.add(i+1, [newL, v - v1])

//                println "j=${j}, i=${i}, v=${v}, v1=${v1}, newL=${newL}, s=${s}"
                continue loop
            }
    }

    //    println s
}
println s


while(s.size() > 1)
    for(int i = 0; i<s.size(); i++) {
        if(s[i][0] == s[i+1][0]) {
            def l = s[i][1]
            def r = s[i+1][1]
            int newL = s[i][0] -1
//            println "l=${l}, r=${r}"

            s.remove(i)
            s.remove(i)
            s.add(i, [newL, 3*l + 2*r])
//            println s
            break
        }
    }
println s