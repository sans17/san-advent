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
    pairs << [
        a,
        matcher[0][2].split(', ') as Set
    ]
}

//println "-20 -> ${ingrs.size()} ${pairs.size()}"
//println "-19 -> ${ingrs} ${pairs}"

pairs = pairs.sort { it[0].size() + it[1].size() }

def foundA = [] as Set
def foundI = [] as Set

//int stop = 10
//for(int i = 0 ; i < stop ; i++) {
//    println "------  ${i} ${pairs.size()}"
pairs.each { println it }

pairs.each { a ->
    pairs.each { b ->
        if(a != b && a[1].size() == 1 && b[1].containsAll(a[1])) a[0] = a[0].intersect(b[0])
    }
}

pairs = pairs.toSet().toList().sort { it[0].size() + it[1].size() }

println "------ ${pairs.size()}"
pairs.each { println it }

for(def same ; (same = pairs.find { it[0].size() == it[1].size() }) != null ; ) {
    println "-10 -> ${same}"

    foundA += same[0]

    pairs.remove(same)

    pairs.each {
        it[0] -= same[0]
        it[1] -= same[1]
    }

    println "0 -> ${pairs.size()}"
    pairs.each { println it }
}

pairs.each {
    foundI += it[0]
}

println "30 -> ${ingrs.toSet().size()} ${foundA.size()} ${foundI.size()}"

def diff = ingrs - foundA
println diff.size()
