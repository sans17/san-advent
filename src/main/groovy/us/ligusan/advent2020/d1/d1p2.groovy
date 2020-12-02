package us.ligusan.advent2020.d1;

List<Integer> list = new File(getClass().getResource('input.txt').toURI()).collect {Integer.valueOf(it)}
list = list.sort()

HashSet<Integer> set = new HashSet(list)

int size = list.size();
for(int i = 0; i < size - 2; i++) for(j = i + 1; j < size - 1; j++) {
    int diff = 2020 - list[i] - list[j]
    if(set.contains(diff)) {
        println "${list[i]} ${list[j]} ${list[i] * list[j] * diff}"
        return
    }
}