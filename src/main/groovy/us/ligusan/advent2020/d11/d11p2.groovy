package us.ligusan.advent2020.d11;

List<String> seatsNext = new File(getClass().getResource('input.txt').toURI()).collect { it }

int sizeX = seatsNext[0].size()
int sizeY = seatsNext.size

//println seatsNext

List<String> seats = []

while(seats != seatsNext){
    seats = seatsNext

    seatsNext = []

    seats.eachWithIndex { row, index ->
        //    println "${index} ${row}"
        String newRow = ''
        row.eachWithIndex { seat, i ->
            char newSeat = seat
            if(newSeat != '.') {
                int occ = 0
                for(int j in -1..1) for(int k in -1..1) if(j != 0 || k != 0)
                    for(int l=1; ; l++) {
                        int x = i + k*l
                        int y = index + j*l
                        if(x < 0 || x >= sizeX || y < 0 || y >= sizeY) break
                            if(seats[y][x] != '.') {
                                if(seats[y][x] == '#') occ++
                                break
                            }
                        //                println "${index} ${i} ${occ}"
                    }

                if(row[i] == 'L' && occ == 0) newSeat = '#'
                else if(row[i] == '#' && occ >= 5) newSeat = 'L'
            }
            newRow += newSeat
        }

        seatsNext << newRow
    }
}

def sums = seats.collect { it.replaceAll('[^#]', '').size() }
println sums.sum()
