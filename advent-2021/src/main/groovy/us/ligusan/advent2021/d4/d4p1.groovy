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
            input << (1..5).collect { (1..5).collect { 1 } }
        }
        nextCard = []
    } else nextCard << line.split()
}

println nums
println cards
//println input

int winIndex
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

//    println "counter=${counter++}, num=${num}"
//    println input
    
    winIndex = input.findIndexOf { flag -> 
        def sums = flag.collect { it.sum() }
        sums += flag.transpose().collect { it.sum() }
        
//        println "sums=${sums}"
        
        sums.contains(0)
    }

    if(winIndex >= 0) break
}

int sum = ([cards[winIndex].flatten().collect { Integer.valueOf(it) }, input[winIndex].flatten()].transpose()*.inject(1) { m, it -> m*it }).sum() 

println Integer.valueOf(winNum) * sum