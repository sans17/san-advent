package us.ligusan.advent2021.d3

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int size = list.size()

def gama0 = list.collect {element -> element.collect {Integer.valueOf(it)} }.transpose()*.sum().collect { (it / size).round().toString() }
int gama = Integer.parseInt(gama0.join(), 2)

int epsilon = (2.power(gama0.size()) - 1) - gama

println "gama=${gama}, epsilon=${epsilon}"
println gama*epsilon
