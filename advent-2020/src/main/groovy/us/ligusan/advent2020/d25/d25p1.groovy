package us.ligusan.advent2020.d25;

def limit = 20_201_227

int loops

int c = 7
BigInteger num = 1
for(int i = 1;; i++) {
    num = num * c % limit

    //        println "-10 -> ${c} ${i} ${num} ${rem.size()}"

    if(num == 9_232_416) {
        println "0 -> ${i}"

        loops = i

        break
    }
}


c = 14_144_084
num = 1

for(int i = 1; i <= loops; i++) num = num * c % limit

println num