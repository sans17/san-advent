package us.ligusan.advent2020.d3;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int xLimit = list.size()
int yLimit = list[0].size()

int multiple = 1;

for(Tuple2 i in [
    new Tuple2(1, 1),
    new Tuple2(1, 3),
    new Tuple2(1, 5),
    new Tuple2(1, 7),
    new Tuple2(2, 1)
]) {
    int y = 0
    int treesNumber = 0
    for(int x = 0; x < xLimit; x += i[0]) {
        if(list[x][y % yLimit] == '#') treesNumber++
        y += i[1]
    }

    println "${i} ${treesNumber}"
    multiple *= treesNumber
}

println multiple
