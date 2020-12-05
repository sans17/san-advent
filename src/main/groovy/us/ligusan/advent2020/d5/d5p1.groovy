package us.ligusan.advent2020.d5;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int maxId = 0
list.each { maxId = Math.max(maxId, Integer.parseInt(it.replaceAll('[FL]', '0').replaceAll('[BR]', '1'), 2)) }
println maxId
