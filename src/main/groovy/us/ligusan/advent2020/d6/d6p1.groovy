package us.ligusan.advent2020.d6;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int sum = 0

int size = list.size()

HashSet<Character> set = []

list.eachWithIndex { element, i ->
    boolean blankFlag = element.isBlank()

    if(!blankFlag) set.addAll(element.toCharArray())

    if(blankFlag || i == size-1) sum += set.size()

    if(blankFlag) set.clear()
}

println sum
