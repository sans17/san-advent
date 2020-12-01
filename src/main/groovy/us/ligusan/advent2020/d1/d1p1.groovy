package us.ligusan.advent2020.d1;

HashSet<Integer> set = new File(getClass().getResource('input.txt').toURI()).collect {Integer.valueOf(it)}

for(int element : set) if(set.contains(2020 - element)) {
    println "${element} ${element * (2020 - element)}"
    break;
}
