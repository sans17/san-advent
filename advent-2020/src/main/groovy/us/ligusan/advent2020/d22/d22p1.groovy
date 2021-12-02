package us.ligusan.advent2020.d22;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {
    it
}

def player1deck = [] as Queue
def player2deck = [] as Queue

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

while(!player1deck.empty && !player2deck.empty) {
    int player1card = player1deck.poll()
    int player2card = player2deck.poll()
    
    if(player1card > player2card) player1deck += [player1card, player2card]
    else player2deck += [player2card, player1card]
}

println player1deck
println player2deck

def winnerDeck = player1deck.empty ? player2deck : player1deck

def sum = winnerDeck.withIndex().sum { card, index -> card * (winnerDeck.size() - index) }

println sum