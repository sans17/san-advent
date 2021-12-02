package us.ligusan.advent2021.d1;

List<Integer> list = new File(getClass().getResource('input.txt').toURI()).collect {Integer.valueOf(it)}

int count = 0

for(int i = 1; i<list.size(); i++) if(list[i-1] < list[i]) count++

println count