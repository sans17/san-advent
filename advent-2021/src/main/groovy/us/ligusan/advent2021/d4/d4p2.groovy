package us.ligusan.advent2021.d4

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

def nums
def cards = []
def input = []
def nextCard

list.each { line ->
    if(nums == null) nums = line.split(',')
    else if("" == line) {
        if(nextCard != null) {
            cards << nextCard
            input << (1..5).collect {
                (1..5).collect {
                    1
                }
            }
        }
        nextCard = []
    } else nextCard << line.split()
}

println nums
println cards
//println input

def winCard
def winInput

String winNum
for(String num in nums) {
    winNum = num

    cards.eachWithIndex { card, i ->
        (0..4).each { x->
            (0..4).each { y ->
                if(card[x][y] == num) input[i][x][y] = 0
            }
        }
    }

    println "num=${num}"
    //    println input

    for(int i = 0; i < cards.size();) {
        def sums = input[i].collect { it.sum() }
        sums += input[i].transpose().collect { it.sum() }

        println "i=${i}, sums=${sums}"

        if(sums.contains(0)) {
            winCard = cards.remove(i)
            winInput = input.remove(i)

            println winCard
            println winInput
        } else i++
    }

    if(cards.isEmpty()) break
}

println winCard
println winInput

int sum = ([
    winCard.flatten().collect { Integer.valueOf(it) },
    winInput.flatten()
].transpose()*.inject(1) { m, it -> m*it }).sum()

println Integer.valueOf(winNum) * sum