package us.ligusan.advent2020.d6;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int sum = 0

int size = list.size()

HashSet<Character> set = []

boolean setEndFlag = true
for(int i=0; i<size; i++) {
    boolean firstLineFlag = setEndFlag
    setEndFlag = list[i].isBlank()

    if(!setEndFlag) {
        def chars = list[i].toCharArray()
        if(firstLineFlag) set.addAll(chars)
        else set.retainAll(chars)
    }

    if(setEndFlag || i == size-1) sum += set.size()

    if(setEndFlag) set.clear()
}

println sum
