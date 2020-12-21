package us.ligusan.advent2020.d20;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def String rightSide(List<String> square, int counter) {
    return (square.collect { it[-counter] }).join()
}
def String leftSide(List<String> square, int counter) {
    return (square.collect { it[counter] }).join()
}


def String topSide(List<String> square) {
    return square[0]
}
def String rightSide(List<String> square) {
    return rightSide(square, 1)
}
def String bottomSide(List<String> square) {
    return square[-1]
}
def String leftSide(List<String> square) {
    return leftSide(square, 0)
}

def List<String> turnLeft(List<String> square) {
    (1..square.size()).collect { rightSide(square, it) }
}
def List<String> turnRight(List<String> square) {
    def newSquare = square.reverse()
    (0..square.size()-1).collect { num -> (newSquare.collect { it[num] }).join() }
}
def List<String> flipVertical(List<String> square) {
    square.collect { it.reverse() }
}
def List<String> flipHorizontal(List<String> square) {
    square.reverse()
}

def List<String> removeBorder(List<String> square) {
    square[1..-2].collect { line -> line.substring(1, line.size() -1) }
}

monster = [
    [0, 18],
    [1, 0],
    [1, 5],
    [1, 6],
    [1, 11],
    [1, 12],
    [1, 17],
    [1, 18],
    [1, 19],
    [2, 1],
    [2, 4],
    [2, 7],
    [2, 10],
    [2, 13],
    [2, 16]
]

def boolean isMonster(List<String> picture, int x, int y) {
    boolean ret = monster.every { picture[y + it[0]][x + it[1]] == '#' }
    if(ret) {
        println " 0: ------ ${x} ${y}"
        (0..2).each { println picture[y + it][x..x+19] }
    }
}


Integer tileNumber = null
Map<Integer, List<String>> tiles = [:]
Map<Integer, List<String>> borders = [:]

List<String> image
list.each { line ->
    if(tileNumber != null)
        if(line.isBlank()) {
            tiles[tileNumber] = image

            //            println " 0 -> ${image}, ${(image.collect { it[-1] }).join()}"

            def border = [
                topSide(image),
                rightSide(image),
                bottomSide(image),
                leftSide(image)
            ]
            //.collect { Integer.parseInt(it.replace('#', '1').replace('.', '0'), 2) }
            border += border.collect { it.reverse() }

            //            println " 0 -> ${border}"

            borders[tileNumber] = border

            tileNumber = null
        }
        else {
            image << line
        }

    if(tileNumber == null) {
        def matcher = line =~ /^Tile (\d+):$/
        if(matcher.find()) {
            //            println "10 -> ${matcher[0]}"

            tileNumber = Integer.parseInt(matcher[0][1])
            image = []
        }
    }
}

Map counter = [:]
borders.each { number, border ->
    border.withIndex().each { borderString, index ->
        counter[borderString] = ((counter[borderString]?:[]) << [number, index])
    }
}

//int start = 1693
//def neis = borders.collectEntries { number, border ->
//    [
//        number,
//        border[0..3].collect { borderString ->
//            counter[borderString].find { it[0] != number } }
//    ]
//}

def processed = [] as Set

//int stop = 25

def matrix = [:]
[0..11, 0..11].combinations().withIndex().each { coord, index ->
    //    println "${index} ${coord}"

    int tileNum

    if(coord == [0, 0]) {
        tileNum = 1693
        matrix[coord] = turnLeft(tiles[tileNum])
    }
    else
        //    if(index < stop)
    {
        boolean secondRowFlag = coord[1] > 0
        def nei = counter[secondRowFlag ? bottomSide(matrix[[coord[0], coord[1] - 1]]) : rightSide(matrix[[coord[0] - 1, coord[1]]]) ].find {
            !processed.contains(it[0]) }
        //        println nei

        tileNum = nei[0]

        List<String> newTile = tiles[tileNum]

        if(secondRowFlag)
            switch(nei[1]) {
                case 0:
                    break

                case 1:
                    newTile = turnLeft(newTile)
                    break

                case 2:
                    newTile = flipHorizontal(newTile)
                    break

                case 3:
                    newTile = flipVertical(turnRight(newTile))
                    break

                case 4:
                    newTile = flipVertical(newTile)
                    break

                case 5:
                    newTile = flipVertical(turnLeft(newTile))
                    break

                case 6:
                    newTile = flipVertical(flipHorizontal(newTile))
                    break

                case 7:
                    newTile = turnRight(newTile)
            }
        else
            switch(nei[1]) {
                case 0:
                    newTile = flipVertical(turnRight(newTile))
                    break

                case 1:
                    newTile = flipVertical(newTile)
                    break

                case 2:
                    newTile = turnRight(newTile)

                case 3:
                    break

                case 4:
                    newTile = flipHorizontal(turnLeft(newTile))
                    break

                case 5:
                    newTile = flipVertical(flipHorizontal(newTile))
                    break

                case 6:
                    newTile = flipVertical(turnLeft(newTile))
                    break

                case 7:
                    newTile = flipHorizontal(newTile)
            }
        matrix[coord] = newTile
    }

    println "${coord} ${tileNum}"

    processed << tileNum

    //    if(index == 24 || index == 12) {
    //        tiles[tileNum].each {
    //            println it
    //        }
    //
    //        println '---'
    //
    //        matrix[coord].each {
    //            println it
    //        }
    //
    //        println counter[topSide(matrix[coord])].find { !processed.contains(it[0]) }
    //        println counter[rightSide(matrix[coord])].find { !processed.contains(it[0]) }
    //        println counter[bottomSide(matrix[coord])].find { !processed.contains(it[0]) }
    //        println counter[leftSide(matrix[coord])].find { !processed.contains(it[0]) }
    //    }
}

picture = []

//picture << ".####...#####..#...###.."
//picture << "#####..#..#.#.####..#.#."
//picture << ".#.#...#.###...#.##.##.."
//picture << "#.#.##.###.#.##.##.#####"
//picture << "..##.###.####..#.####.##"
//picture << "...#.#..##.##...#..#..##"
//picture << "#.##.#..#.#..#..##.#.#.."
//picture << ".###.##.....#...###.#..."
//picture << "#.####.#.#....##.#..#.#."
//picture << "##...#..#....#..#...####"
//picture << "..#.##...###..#.#####..#"
//picture << "....#.##.#.#####....#..."
//picture << "..##.##.###.....#.##..#."
//picture << "#...#...###..####....##."
//picture << ".#.##...#.##.#.#.###...#"
//picture << "#.###.#..####...##..#..."
//picture << "#.###...#.##...#.######."
//picture << ".###.###.#######..#####."
//picture << "..##.#..#..#.#######.###"
//picture << "#.#..##.########..#..##."
//picture << "#.#####..#.#...##..#...."
//picture << "#....##..#.#########..##"
//picture << "#...#.....#..##...###.##"
//picture << "#..###....##.#...##.##.#"

for(int j in 0..11) {
    def a
    for(int i in 0..11) {
        def noBorder = removeBorder(matrix[[i, j]])
        a = i == 0 ? noBorder : [a, noBorder].transpose()*.join()
        //    println a
    }
    picture += a
}



picture = flipHorizontal(picture)
picture.each {
    println it
}

println "${picture.size()} ${picture[0].size()}"

int size = picture.size()

int monsterCount = 0
(0..(size-19)).each { x ->
    (0..(size-2)).each { y ->
        if(isMonster(picture, x, y)) {
            monsterCount++
            println monsterCount
        }
    }
}
println monsterCount

int num = picture.sum { it.count('#') }

println picture.sum { it.count('.') }

println "${num} ${num - monsterCount * monster.size()}"