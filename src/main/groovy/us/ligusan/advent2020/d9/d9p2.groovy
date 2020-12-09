package us.ligusan.advent2020.d9;

List<Long> list = new File(getClass().getResource('input.txt').toURI()).collect { Long.parseLong(it) }

long find = 1212510616

int size = list.size()
for(int i = 0; i<size -1; i++)
    for(int j = i+1; j<size; j++) {
        long sum = list[i..j].sum()
        if(sum == find) {
            println "${list[i..j].min() + list[i..j].max()}"
            return
        }
        if(sum > find) break
    }