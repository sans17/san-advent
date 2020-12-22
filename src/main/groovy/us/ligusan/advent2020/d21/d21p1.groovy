package us.ligusan.advent2020.d21;

//def pairs = [
//    [['mxmxvkd', 'kfcds', 'sqjhc', 'nhms'] as Set, ['dairy', 'fish'] as Set],
//    [['trh', 'fvjkl', 'sbzzf', 'mxmxvkd'] as Set, ['dairy'] as Set],
//    [['sqjhc', 'fvjkl'] as Set, ['soy'] as Set],
//    [['sqjhc', 'mxmxvkd', 'sbzzf'] as Set, ['fish'] as Set]] as Queue
//def ingrs = pairs.collectMany {
//    it[0]
//}
//println "-30 -> ${ingrs}"


List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def pairs = []
def ingrs = []

list.each { line ->
    def matcher = line =~ /^(.+) \(contains (.+)\)$/
    matcher.find()

    def a = matcher[0][1].split(' ') as Set

    ingrs += a
    pairs << [a, matcher[0][2].split(', ') as Set]
}

//println "-20 -> ${ingrs.size()} ${pairs.size()}"
//println "-19 -> ${ingrs} ${pairs}"


pairs.sort { it[1].size() }
pairs.each { println it }


//def found = [] as Set
//
//int i = 0
//int stop = 10
//for(def pair; !pairs.isEmpty() && i < stop; i++) {
//    pairs.sort { it[0].size() }
//
//    pair = pairs.pop()
//
//    println "-10 -> ${pair[0].size()} ${pair[1].size()} ${pair}"
//
////    if(pair[1].isEmpty()){
////        if(!pair[0].isEmpty())
////            pairs.each {
////                it[0].removeAll(pair[0])
////
////                println "-9 -> ${it[0].size()} ${it[1].size()} ${it}"
////            }
////    }
////    else  {
//        if(pair[0].size() == pair[1].size()) {
//            found += pair[0]
//
//            pairs.each {
//                it[0].removeAll(pair[0])
//                it[1].removeAll(pair[1])
//
//                println "-8 -> ${it[0].size()} ${it[1].size()} ${it}"
//            }
//        }
//        else {
//            boolean noIntersections = true
//
//            def res = [] as Set
//            pairs.each {
//                if(it[1].containsAll(pair[1]))
//                    
//                
//                                def a = pair[0].intersect(it[0])
//                def b = pair[1].intersect(it[1])
//
//                if(!a.isEmpty() || !b.isEmpty())
//                {
//                    noIntersections = false
//
//                    def diff = [[a, b], [pair[0] - a, pair[1] - b], [it[0] - a, it[1] - b]].findAll { !it[0].isEmpty() && !it[1].isEmpty() }
//                    println "0 -> ${diff}"
//
//                    res += diff
//                }
//            }
//
//            if(noIntersections) found += pair[0]
//            else {
//                pairs += res
//                pairs = pairs.toSet().toList()
//            }
//        }
//    }
//
//    println "10 -> ${i} ${pairs.size()} ${found.size()}"
//    println "11 -> ${i} ${pairs} ${found}"
//}
//
//println found
//
//ingrs.removeAll(found)
//
//println ingrs.size()