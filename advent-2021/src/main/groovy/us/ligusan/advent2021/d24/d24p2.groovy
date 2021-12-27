package us.ligusan.advent2021.d24

//def w= [ 1,  1,  1,  1,  1,   1,   1,  1,  3,  1,  6,  9,   9,  9]
ax=[14, 14, 14, 12, 15, -12, -12, 12, -7, 13, -8, -5, -10, -7]
ay=[14,  2,  1, 13,  5,   5,   5,  9,  3, 13,  2,  1,  11,  8]
dz=[ 1,  1,  1,  1,  1,  26,  26,  1, 26,  1, 26, 26,  26, 26]
//println "w=${w}, ax=${ax}, ay=${ay}, dz=${dz}"
//println ax.sum()

def calc(z, w, i) {
//    println "z=${z}, w=${w}, i=${i}, ax[i]=${ax[i]}, ay[i]=${ay[i]}, dz[i]=${dz[i]}"
    def x0 = z.mod(26)
    def x1 = x0 + ax[i]
    def x = x1 == w ? 0:1
    def y0 = (w+ay[i])*x
//    println "x0=${x0}, x1=${x1}, x=${x}, y0=${y0}"
    z=z.divide(dz[i])*(25*x+1) + y0
//    println "i=${i}, z=${z}"
//    println '----------------'
    return z
}

//def z = BigInteger.ZERO
//println calc(new BigInteger(435), 9, 12)

//def zs = [0..26*26*26, 1..9, 1..9, 1..9].combinations().findAll {
//    def c = calc(calc(calc(new BigInteger(it[0]), it[1], 11), it[2], 12), it[3], 13)
////    println "it=${it}, c=${c}"
//    return c == 0
//}
//def zs = [0..26*26, 1..9, 1..9].combinations().findAll {
//    def c = calc(calc(new BigInteger(it[0]), it[1], 12), it[2], 13)
////    println "it=${it}, c=${c}"
//    return c == 0
//}
def resMap = [:]
resMap[BigInteger.ZERO]=''
loop: while(!resMap.empty) {
    def newResMap = [:]

    def values = resMap.keySet()
    def maxValue = values.max()
    int len = resMap[maxValue].size()
    
    println "maxValue=${maxValue}, len=${len}, newResMap.size=${newResMap.size()}"
    outer: for(BigInteger start = 0; start <= (maxValue + 1)*26; start++) for(int w=1; w<10; w++) {
        def path = resMap[calc(start, w, 13 - len)]
        if(path != null) {
            def newPath = w.toString() + path
            println "start=${start}, newPath=${newPath}"

            if(len == 13 && start == 0) {
                println "newPath=${newPath}"
                break loop
            } else {
                newResMap[start] = newPath
                continue outer
            }
        }
    }

    resMap = newResMap
}


//def s = 435
//def zs = [s*10..(s+1)*26, 1..9].combinations().findAll { zw ->
//    def z = new BigInteger(zw[0])
//    (3..0).each {
//        int iter = 13 - it
//        z = calc(z, it == 3 ? zw[1] : w[1], iter)
//    }
////    println "it=${it}, c=${c}"
//    return z == 0
//} 

//println zs.size()
//println zs
//println (zs.sort { it[0] * 100 + it[1] * 10 + it[2] })
//println ((zs.collect { it[0] }).toSet().sort())
//println ((zs.collect { it[0] }).max())

