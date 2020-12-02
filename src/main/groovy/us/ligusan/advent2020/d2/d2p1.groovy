package us.ligusan.advent2020.d2;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int correctNumber = 0
outer: for(String element : list) {
    def split = (element =~ /(\d+)-(\d+) ([a-z]): ([a-z]+)/)[0]
    
    int upperLimit = Integer.parseInt(split[2])
    
    def matcher = split[4] =~ /${split[3]}/
    int counter = 0
    while(matcher.find()) if(++counter > upperLimit) continue outer
    
    if(counter >= Integer.parseInt(split[1])) correctNumber++
}

println correctNumber
