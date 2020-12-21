package us.ligusan.advent2020.d20;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

Integer tileNumber = null
Map<Integer, List<String>> tiles = [:]
Map<Integer, List<String>> borders = [:]

List<String> image
list.each { line ->
    if(tileNumber != null)
        if(line.isBlank()) {
            tiles[tileNumber] = image

            //            println " 0 -> ${image}, ${(image.collect { it[-1] }).join()}"

            def border = [
                image[0],
                (image.collect { it[-1] }).join(),
                image[-1].reverse(),
                (image.collect { it[0] }).join().reverse()
            ]
            //.collect { Integer.parseInt(it.replace('#', '1').replace('.', '0'), 2) }
            border += border.collect { it.reverse() }

            //            println " 0 -> ${border}"

            borders[tileNumber] = border

            tileNumber = null
        }
        else {
            image << line
        }

    if(tileNumber == null) {
        def matcher = line =~ /^Tile (\d+):$/
        if(matcher.find()) {
            //            println "10 -> ${matcher[0]}"

            tileNumber = Integer.parseInt(matcher[0][1])
            image = []
        }
    }
}

Map counter = [:]
borders.values().each { border ->
    border.each {
        counter[it] = (counter[it]?:0) + 1
    }
}

Map borderCounter = borders.collectEntries { entry ->
    [
        entry.key,
        entry.value.collect {
            counter[it] }
    ]
}

Long res = null
borderCounter.each { //    println it
    number, counters ->
    if(counters.count(1) > 2) {
        println "${number}, ${counters}"
        res = res == null ? number : res * number
    }
}
println res