package us.ligusan.advent2020.d7;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

bagsTree = [:]

int numberOfBags(final String outerBag) {
    int ret = 1
    
    bagsTree[outerBag].each {key, value ->
        ret += value * numberOfBags(key)
    }
    
    return ret
}

list.each {
    def matcher = it =~ /([^ ]+ [^ ]+) bags contain (.*)/
    
    HashMap<String, Integer> innerBags = []
    (matcher[0][2] =~ /([0-9]+) ([^ ]+ [^ ]+) bag/).each {
        innerBags[it[2]] = Integer.valueOf(it[1])
    }
    
    bagsTree[matcher[0][1]] = innerBags
}

println numberOfBags('shiny gold') -1
