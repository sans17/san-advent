package us.ligusan.advent2022.d21

import java.util.regex.Matcher

list = new File(getClass().getResource('input.txt').toURI()).collect { it }

println "list=$list"

n = -1
outer: for(j=0; ; j++) {
    size = list.size()
    println "j=$j, size=$size"
    inner: for(i=0; i<size; i++) {
//        println "list[$i]=${list[i]}"
        line = list[i]
        name = null
        value = null

        l = null
        r = null
        switch(line) {
            case ~/root: ([a-z]+) . (\d+)/:
                list.remove(i)

                name = Matcher.lastMatcher[0][1]
                value = Matcher.lastMatcher[0][2]
                break

            case ~/root: (\d+) . ([a-z]+)/:
                list.remove(i)

                name = Matcher.lastMatcher[0][2]
                value = Matcher.lastMatcher[0][1]
                break

            case ~/([a-z]+): (\d+)/:
                list.remove(i)
                
                if(Matcher.lastMatcher[0][1] == 'humn') {
                    println "i=$i, line=$line"
                    println "list.size=${list.size()} list=$list"
                    break inner
                }
                
                name = Matcher.lastMatcher[0][1]
                value = Matcher.lastMatcher[0][2]
                break

            case ~/(\d+): ([a-z]+)/:
                list.remove(i)

                name = Matcher.lastMatcher[0][2]
                value = Matcher.lastMatcher[0][1]
                break

            case ~/([a-z]+): (\d+) ([-+*\/]) (\d+)/:
//                println "lastMatcher=${Matcher.lastMatcher[0]}"
                list.remove(i)
                
                name = Matcher.lastMatcher[0][1]
                l = new BigInteger(Matcher.lastMatcher[0][2])
                r = new BigInteger(Matcher.lastMatcher[0][4])
//                println "l=$l, r=$r"
                switch(Matcher.lastMatcher[0][3]) {
                    case '+':
                        value = l + r
                        break
                    
                    case '-':
                        value = l - r
                        break

                    case '*':
                        value = l * r
                        break

                    case '/':
                        value = l / r
                        break
                }
                break
                
            case ~/(\d+): ([a-z]+) ([-+*\/]) (\d+)/:
                list.remove(i)
                
                name = Matcher.lastMatcher[0][2]
                l = new BigInteger(Matcher.lastMatcher[0][1])
                r = new BigInteger(Matcher.lastMatcher[0][4])
                switch(Matcher.lastMatcher[0][3]) {
                    case '+':
                        value = l - r
                        break
                    
                    case '-':
                        value = l + r
                        break

                    case '*':
                        value = l / r
                        break

                    case '/':
                        value = l * r
                        break
                }
                break

            case ~/(\d+): (\d+) ([-+*\/]) ([a-z]+)/:
                list.remove(i)
                
                name = Matcher.lastMatcher[0][4]
                l = new BigInteger(Matcher.lastMatcher[0][1])
                r = new BigInteger(Matcher.lastMatcher[0][2])
                switch(Matcher.lastMatcher[0][3]) {
                    case '+':
                        value = l - r
                        break
                    
                    case '-':
                        value = r - l
                        break

                    case '*':
                        value = l / r
                        break

                    case '/':
                        value = r / l
                        break
                }
                break

            default:
                continue
        }

        println "name=$name, value=$value"
        if(name == 'humn') {
            n = value
            break outer
        }
        
        list = list.collect { it.replaceAll(name, value.toString()) }
        println "i=$i, line=$line"
        println "list.size=${list.size()} list=$list"
        break
    }
    
    if(list.size() == size) break
}
println n