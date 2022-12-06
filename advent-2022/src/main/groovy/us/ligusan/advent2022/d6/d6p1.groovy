package us.ligusan.advent2022.d6

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

a = Arrays.asList(list[0].toCharArray())

for(int i = 0 ; i <= a.size()-4; i++) {
    s = a[i..(i+3)]
    println "i=$i, s=$s"
    if((s as Set).size() == 4) {
        println i + 4;
        break;
    }
}
