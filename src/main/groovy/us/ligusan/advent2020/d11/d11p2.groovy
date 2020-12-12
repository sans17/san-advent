package us.ligusan.advent2020.d11;

def seatsNext = new File(getClass().getResource('input.txt').toURI()).collect { Arrays.asList(it.toCharArray()) }

int sizeX = seatsNext[0].size()
int sizeY = seatsNext.size

for(def seats = []; seats != seatsNext;){
    seats = seatsNext

    //    println '-------'

    seatsNext = seats.withIndex().collect { row, rowIndex ->
        //        println "${row.join()}"
        row.withIndex().collect { seat, seatIndex ->
            if(seat != '.') {
                int occ = 0
                for(int i in -1..1) for(int j in -1..1) if(i != 0 || j != 0) for(int r=1; ; r++) {
                    int x = seatIndex + j*r
                    int y = rowIndex + i*r
                    if(x < 0 || x >= sizeX || y < 0 || y >= sizeY) break

                        if(seats[y][x] != '.') {
                            if(seats[y][x] == '#') occ++
                            break
                        }
                    //                println "${index} ${i} ${occ}"
                }

                if(seat == 'L' && occ == 0) return '#'
                if(seat == '#' && occ >= 5) return 'L'
            }
            seat
        }
    }
}

println seatsNext.sum { it.count('#') }
