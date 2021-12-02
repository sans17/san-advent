package us.ligusan.advent2020.d23;

def circle = [8, 5, 3, 1, 9, 2, 6, 4, 7]
//def circle = [3, 8, 9, 1, 2, 5, 4, 6, 7]

def int rotate(List list) {
    int ret = list.pop()
    list << ret
    ret
}

(1..100).each {
    int current = rotate(circle)
    def pick = [circle.pop(), circle.pop(), circle.pop()]

    int index
    for(int dest = current; (index = circle.indexOf(dest = (dest == 1 ? 9 : dest -1))) < 0; );

        while(!pick.empty) circle.add(++index, pick.pop())

//    println circle
}

while(circle[0] > 1) rotate(circle)
    
println circle[1..-1].join()