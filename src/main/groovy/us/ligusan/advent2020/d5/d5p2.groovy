package us.ligusan.advent2020.d5;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int maxId = 0

HashSet<Integer> ids = []
list.each {
    int id = Integer.parseInt(it.replaceAll('[FL]', '0').replaceAll('[BR]', '1'), 2)
    ids << id
    maxId = Math.max(maxId, id)
}

for(int i = 1; i < maxId; i++)
    if(!ids.contains(i) && ids.contains(i -1) && ids.contains(i +1)) {
        println i
        return
    }
