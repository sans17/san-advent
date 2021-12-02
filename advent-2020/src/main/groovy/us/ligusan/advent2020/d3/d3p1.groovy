package us.ligusan.advent2020.d3;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int y = 0

int treesNumber = 0
for(String element : list) {
    if(element[y % element.size()] == '#') treesNumber++
    y+=3
}

println treesNumber
