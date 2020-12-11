package us.ligusan.advent2020.d11;

List<List<Integer>> seatsNext = new File(getClass().getResource('input.txt').toURI()).collect { line ->
    List<Integer> row = [-1]
    row += line.toCharArray().collect { seatChar -> seatChar == 'L' ? 0 : -1 }
    row << -1
}

int sizeX = seatsNext[0].size() - 1
List<Integer> emptyRow = (0..sizeX).collect { -1 }
seatsNext.add(0, emptyRow)
seatsNext << emptyRow

int sizeY = seatsNext.size - 1

//println seatsNext

List<List<Integer>> seats = []

while(seats != seatsNext){
    seats = seatsNext

    //    println "------------ ${steps}"
    //    def a = seats.collect { (it.collect { it < 0 ? '.' : it == 0 ? 'L' : '#' }).join('') }
    //    //println a
    //    println a.join('\n')

    seatsNext = []

    seats.eachWithIndex { row, index ->
        //    println "${index} ${row}"
        if(index == 0 || index == sizeY) seatsNext << row
        else {
            List<Integer> newRow = [-1]
            for(int i in 1..sizeX-1) {
                int newSeat = row[i]
                if(newSeat >= 0) {
                    int occ = 0
                    for(int j in -1..1) for(int k in -1..1) if(j != 0 || k != 0)
                        for(int l=1; ; l++) {
                            int x = i + k*l
                            int y = index + j*l
                            if(x < 1 || x >= sizeX || y < 1 || y >= sizeY) break
                                if(seats[y][x] >= 0) {
                                    occ += seats[y][x]
                                    break
                                }
                            //                println "${index} ${i} ${occ}"
                        }

                    if(row[i] == 0 && occ == 0) newSeat = 1
                    else if(row[i] == 1 && occ >= 5) newSeat = 0
                }
                newRow << newSeat
            }
            newRow << -1

            seatsNext << newRow
        }
    }
}

def sums = seats.collect { row -> row.sum { Math.max(0, it) } }
println sums.sum()
