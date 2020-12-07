package us.ligusan.advent2020.d7;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

bagsTree = [:]

list.each {
    def matcher = it =~ /([^ ]+ [^ ]+) bags contain (.*)/

    HashSet<String> innerBags = []
    (matcher[0][2] =~ /([0-9]+) ([^ ]+ [^ ]+) bag/).each {
        innerBags << it[2]
    }

    bagsTree[matcher[0][1]] = innerBags
}


totalContainers = [] as Set

bagsToHold = ['shiny gold'] as Queue

for(String bag; (bag = bagsToHold.poll()) != null;) {
    bagsTree.each { key, value ->
        if(value.contains(bag) && totalContainers.add(key)) bagsToHold << key
    }
}

println totalContainers.size()
