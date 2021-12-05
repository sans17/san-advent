package us.ligusan.advent2021.d1

List<Integer> list = new File(getClass().getResource('input.txt').toURI()).collect {Integer.valueOf(it)}

int count = 0

for(int i = 2; i < list.size() - 1; i++) {
	if(list[i-2] < list[i+1]) count++
}
println count