package us.ligusan.advent2022.d6

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

l = 14
a = Arrays.asList(list[0].toCharArray())

for(int i = 0 ; i <= a.size()-l; i++) {
    s = a[i..(i+l-1)]
    println "i=$i, s=$s"
    if((s as Set).size() == l) {
        println i + l;
        break;
    }
}
