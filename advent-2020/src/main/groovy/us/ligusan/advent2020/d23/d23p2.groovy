package us.ligusan.advent2020.d23;

//def circle = [3:8, 8:9, 9:1, 1:2, 2:5, 5:4, 4:6, 6:7, 7:3]
//int current = 3

int limit = 1_000_000
def circle = [8:5, 5:3, 3:1, 1:9, 9:2, 2:6, 6:4, 4:7, 7:10]
(10..limit-1).each {
    circle[it] = it+1
}
circle[limit]=8
int current = 8
    
for (i in 1..10_000_000) {
    int next = current
    def pick = (1..3).collect { next = circle.remove(next) }
    circle[current] = circle.remove(next)

    int dest = current
    while(pick.contains(dest = (dest == 1 ? limit : dest -1)));

    current = circle[current]

//    println "0 -> ${pick} ${dest} ${circle} ${current}"
    
    next = circle.remove(dest)
    
    while(!pick.empty) {
        int pop = pick.pop()
        circle[dest] = pop
        dest = pop
    }
    circle[dest] = next

//    println i
//    println "${current} ${circle}"
}

int one = circle[1]
long two = circle[one]
println "${one} ${two}"
println one * two