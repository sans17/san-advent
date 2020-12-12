package us.ligusan.advent2020.d12;

List<Tuple2<String, Integer>> instructions = new File(getClass().getResource('input.txt').toURI()).collect {
    matcher = it =~ /(N|S|E|W|L|R|F)(\d+)/

    [
        matcher[0][1],
        Integer.valueOf(matcher[0][2])
    ]
}

int pointX = 10
int pointY = 1

int x = 0
int y = 0

instructions.each {
    //    println " 0: ${it}"

    switch(it[0]) {
        case 'N':
            pointY += it[1]
            break
        case 'S':
            pointY -= it[1]
            break
        case 'E':
            pointX += it[1]
            break
        case 'W':
            pointX -= it[1]
            break
        case 'L':
        case 'R':
            if(it[1] == 180) {
                pointX = -pointX
                pointY = -pointY
            } else {
                int oldPointX = pointX
                boolean leftFlag = it[0] == 'L' && it[1] == 90 || it[0] == 'R' && it[1] == 270

                pointX = (leftFlag ? -1 : 1) * pointY
                pointY = (leftFlag ? 1 : -1) * oldPointX
            }
            break
        case 'F':
            x += pointX * it[1]
            y += pointY * it[1]
    }


    //    println "10: ${pointX} ${pointY} ${x} ${y}"
}

println "${x} ${y} ${Math.abs(x) + Math.abs(y)}"
