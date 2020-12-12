package us.ligusan.advent2020.d11;

def seatsNext = new File(getClass().getResource('input.txt').toURI()).collect { Arrays.asList(it.toCharArray()) }

int sizeX = seatsNext[0].size()
int sizeY = seatsNext.size

//println seatsNext

for(def seats = []; seats != seatsNext;){
    seats = seatsNext

    seatsNext = seats.withIndex().collect { row, rowIndex ->
        //    println "${index} ${row}"
        row.withIndex().collect { seat, seatIndex ->
            char newSeat = seat
            if(newSeat != '.') {
                int occ = 0
                for(int i in -1..1) for(int j in -1..1) if(i != 0 || j != 0)
                {
                    int x = seatIndex + j
                    int y = rowIndex + i
                    if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && seats[y][x] == '#') occ++
                }

                if(row[seatIndex] == 'L' && occ == 0) newSeat = '#'
                else if(row[seatIndex] == '#' && occ >= 5) newSeat = 'L'
            }
            newSeat
        }
    }
}

println seatsNext.sum { it.count('#') }
