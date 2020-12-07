package us.ligusan.advent2020.d6;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int sum = 0

int size = list.size()

HashSet<Character> set = []
for(int i=0; i<size; i++) {
    boolean blankFlag = list[i].isBlank()

    if(!blankFlag) set.addAll(list[i].toCharArray())

    if(blankFlag || i == size-1) {
        println set
        sum+=set.size()
    }

    if(blankFlag) set.clear()
}

println sum
