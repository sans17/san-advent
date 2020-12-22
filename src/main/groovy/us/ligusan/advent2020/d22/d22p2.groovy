package us.ligusan.advent2020.d22;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {
    it
}

already = [:]

step = 0

def play(int level, List deck1, List deck2) {
    //    println "0 -> ${level} ${deck1} ${deck2}"
    def clone1 = deck1.clone()
    def clone2 = deck2.clone()

    def alreadyDone = already[[clone1, clone2]]
    if(alreadyDone != null) {
        //        println "1 -> ${level}"
        return alreadyDone
    }
    alreadyDone = already[[clone2, clone1]]
    if(alreadyDone != null) {
        //        println "2 -> ${level}"
        return alreadyDone.reverse()
    }


    def done1 = [] as Set
    def done2 = [] as Set

    while(!deck1.empty && !deck2.empty)
        if(done1.contains(deck1) || done2.contains(deck2)) {
            //            println "3 -> ${level}"
            deck1 += deck2
            deck2 = []
        } else {
            done1 << deck1.clone()
            done2 << deck2.clone()
            //        done1.each {
            //            println "4 -> ${level} ${it}"
            //        }
            //        done2.each {
            //            println "4.5 -> ${level} ${it}"
            //        }


            int card1 = deck1.pop()
            int card2 = deck2.pop()
            //        println "5 -> ${level} ${card1} ${card2}"

            if((card1 <= deck1.size() && card2 <= deck2.size()) ? play(level+1, deck1[0..card1-1].clone(), deck2[0..card2-1].clone())[1].empty : card1 > card2) deck1 += [card1, card2]
            else deck2 += [card2, card1]
            //        println "7 -> ${level} ${deck1} ${deck2}"
        }

//    println "10 -> ${level} ${step++} ${deck1} ${deck2}"
    already[[clone1, clone2]] = [deck1.clone(), deck2.clone()]

    return [deck1, deck2]
}

//def player1deck = [9, 2, 6, 3, 1]
//def player2deck = [5, 8, 4, 7, 10]

def player1deck = []
def player2deck = []

boolean player1Flag = false
boolean player2Flag = false
list.eachWithIndex { line, lineIndex ->
    println "${lineIndex} ${line}"

    if(line.isBlank()) player1Flag = player2Flag = false
    else if(line == 'Player 1:') player1Flag = true
    else if(line == 'Player 2:') player2Flag = true
    else (player1Flag ? player1deck : player2deck) << Integer.parseInt(line)
}

println player1deck
println player2deck

(player1deck, player2deck) = play(0, player1deck, player2deck)

println player1deck
println player2deck

def winnerDeck = player1deck.empty ? player2deck : player1deck

def sum = winnerDeck.withIndex().sum { card, index -> card * (winnerDeck.size() - index) }

println sum