package us.ligusan.advent2020.d11;

List<String> seatsNext = new File(getClass().getResource('input.txt').toURI()).collect { it }

int sizeX = seatsNext[0].size()
int sizeY = seatsNext.size

//println seatsNext

List<String> seats = []

while(seats != seatsNext){
    seats = seatsNext

    //    println "------------ ${steps}"
    //    def a = seats.collect { (it.collect { it < 0 ? '.' : it == 0 ? 'L' : '#' }).join('') }
    //    //println a
    //    println a.join('\n')

    seatsNext = []

    seats.eachWithIndex { row, index ->
        String newRow = ''
        row.eachWithIndex { seat, i ->
            char newSeat = seat
            if(newSeat != '.') {
                int occ = 0
                for(int j in -1..1) for(int k in -1..1) if(j != 0 || k != 0)
                {
                    int x = i + k
                    int y = index + j
                    if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && seats[y][x] == '#') occ++

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
