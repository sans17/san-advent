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

                int occ = -Math.max(0, row[i])
                for(int j in index-1..index+1) for(int k in i-1..i+1) occ += Math.max(0, seats[j][k])
                //                println "${index} ${i} ${occ}"

                if(row[i] == 0 && occ == 0) newSeat = 1
                else if(row[i] == 1 && occ >= 4) newSeat = 0
                newRow << newSeat
            }
            newRow << -1

            seatsNext << newRow
        }
    }
}

def sums = seats.collect { row -> row.sum { Math.max(0, it) } }
println sums.sum()
