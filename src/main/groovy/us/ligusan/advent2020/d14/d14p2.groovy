package us.ligusan.advent2020.d14;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

String mask
int maskXlength
HashSet<String> addresses
HashMap<String, BigInteger> mem = [:]

list.each { line ->
    def matcher = line =~ /^mask = ([01X]+)$/
    if(matcher.find()) {
        mask = matcher[0][1]

        maskXlength = mask.count('X')
        //        println "${line} ${mask} ${maskXlength}"
    } else {
        matcher = line =~ /^mem\[(\d+)\] = (\d+)$/
        matcher.find()

        String address = Integer.toBinaryString(Integer.parseInt(matcher[0][1])).padLeft(mask.size(), '0')
        BigInteger value = new BigInteger(matcher[0][2])
        //        println "${address} ${value}"

        addresses = []
        for(int i = 0; i < Math.pow(2, maskXlength); i++){
            def queue = Arrays.asList(Integer.toBinaryString(i).padLeft(maskXlength, '0').toCharArray()) as Queue
            //            println "0: ${i} ${queue}"

            addresses << (Arrays.asList(mask.toCharArray()).withIndex().collect { maskChar, index ->
                maskChar == '1' ? '1' : maskChar == 'X' ? queue.poll() : address[index]
            }).join()
        }
        //        println "${address} ${addresses}"

        addresses.each {
            mem[it] = value
        }
    }
}

//println mem

println mem.values().sum()