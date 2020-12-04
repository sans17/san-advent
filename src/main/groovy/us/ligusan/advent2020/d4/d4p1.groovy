package us.ligusan.advent2020.d4;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int correctNumber = 0

int size = list.size()

List<String> requiredFields = [
    'byr',
    'iyr',
    'eyr',
    'hgt',
    'hcl',
    'ecl',
    'pid'
]
HashSet<String> fields = new HashSet();
for(int i=0; i<size; i++) {
    boolean blankFlag = list[i].isBlank()

    if(!blankFlag) (list[i] =~ /([a-z]{3}):([^ ]+)/).each { fields << it[1] }

    if((blankFlag || i == size-1) && fields.containsAll(requiredFields)) correctNumber++

    if(blankFlag) fields.clear()
}

println correctNumber
