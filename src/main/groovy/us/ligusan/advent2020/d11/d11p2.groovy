package us.ligusan.advent2020.d11;

List<String> seatsNext = new File(getClass().getResource('input.txt').toURI()).collect { it }

int sizeX = seatsNext[0].size()
int sizeY = seatsNext.size

//println seatsNext

List<String> seats = []

while(seats != seatsNext){
    seats = seatsNext

    seatsNext = []

    seats.eachWithIndex { row, rowIndex ->
        //    println "${index} ${row}"
        String newRow = ''
        row.eachWithIndex { seat, seatIndex ->
            char newSeat = seat
            if(newSeat != '.') {
                int occ = 0
                for(int i in -1..1) for(int j in -1..1) if(i != 0 || j != 0)
                    for(int r=1; ; r++) {
                        int x = seatIndex + j*r
                        int y = rowIndex + i*r
                        if(x < 0 || x >= sizeX || y < 0 || y >= sizeY) break
                            if(seats[y][x] != '.') {
                                if(seats[y][x] == '#') occ++
                                break
                            }
                        //                println "${index} ${i} ${occ}"
                    }

                if(row[seatIndex] == 'L' && occ == 0) newSeat = '#'
                else if(row[seatIndex] == '#' && occ >= 5) newSeat = 'L'
            }
            newRow += newSeat
        }

        seatsNext << newRow
    }
}

println seats.sum { it.count('#') }
