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
            input << (1..5).collect { (1..5).collect {0} }
        }
        nextCard = []
    } else nextCard << line.split()
}

println nums
println cards
//println input

int ret
int counter = 0
for(String num : nums) {
    cards.eachWithIndex { card, i ->
        (0..4).each { x->
            (0..4).each { y ->
                if(card[x][y] == num) input[i][x][y] = Integer.valueOf(num)
            }
        }
    }

//    println "counter=${counter}"
//    println input
    
    ret = (input.collect { card ->
        def multi = card.collect { h -> h.inject(1) { m, it -> m*it } }
        multi += card.transpose().collect { v -> v.inject(1) { m, it -> m*it } }
        
        multi.sum()
    }).sum()

//    println ret
        
    if(ret > 0) break
}

println ret
