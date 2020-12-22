package us.ligusan.advent2020.d21;

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


pairs = pairs.sort { it[0].size() + it[1].size() }

TreeMap<String, String> foundA = [:]
def foundI = [] as Set

pairs.each { println it }

pairs.each { a ->
    pairs.each { b ->
        if(a != b && a[1].size() == 1 && b[1].containsAll(a[1])) a[0] = a[0].intersect(b[0])
    }
}

pairs = pairs.toSet().toList().sort { it[0].size() + it[1].size() }

println "------ ${pairs.size()}"
pairs.each { println it }

for(def same ; (same = pairs.find { it[0].size() == 1 && it[1].size() == 1 }) != null ; ) {
    println "-10 -> ${same}"

    foundA[(same[1].toList())[0]] = (same[0]).toList()[0]

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

println foundA.values().join(',')
