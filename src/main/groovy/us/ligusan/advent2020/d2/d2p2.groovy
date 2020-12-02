package us.ligusan.advent2020.d2;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int correctNumber = 0
for(String element : list) {
    def split = (element =~ /(\d+)-(\d+) ([a-z]): ([a-z]+)/)[0]
    
    char letter = split[3][0]
    if(split[4][Integer.parseInt(split[1])-1] == letter ^ split[4][Integer.parseInt(split[2])-1] == letter) correctNumber++
}

println correctNumber
