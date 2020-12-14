package us.ligusan.advent2020.d14;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

String mask
HashMap<String, BigInteger> mem = [:]

list.each { line ->
    def matcher = line =~ /^mask = ([01X]+)$/
    if(matcher.find()) {
        mask = matcher[0][1]
        
//        println "${line} ${mask}"
    } else {
        matcher = line =~ /^mem\[(\d+)\] = (\d+)$/
        matcher.find()

        String value = Integer.toBinaryString(Integer.parseInt(matcher[0][2])).padLeft(mask.size(), '0')

        String newVlaue = (Arrays.asList(mask.toCharArray()).withIndex().collect { valChar, index ->
            valChar == 'X' ? value[index] : valChar
        }).join()
//        println "${line} ${newVlaue}"

        mem[matcher[0][1]] = new BigInteger(newVlaue, 2)
    }
}

//println mem

println mem.values().sum()