package us.ligusan.advent2020.d1;

HashSet<Integer> set = new File(getClass().getResource('input.txt').toURI()).collect {Integer.valueOf(it)}

for(int element : set) {
    int diff = 2020 - element
    if(set.contains(diff)) {
        println "${element} ${element * diff}"
        break;
    }
}
