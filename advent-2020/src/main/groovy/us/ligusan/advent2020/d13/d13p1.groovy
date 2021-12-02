package us.ligusan.advent2020.d13;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

long time = Long.parseLong(list[0])

println time

def matcher = list[1] =~ /(^|,)(\d+)(,|$)/
matcher.find()

def a = matcher[0..-1].collect {
    int bus = Integer.parseInt(it[2])

    [bus, bus - time % bus]
}

def min = a.min { it[1] }

println "${min} ${min[0] * min[1]}"