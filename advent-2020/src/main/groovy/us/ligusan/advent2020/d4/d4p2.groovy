package us.ligusan.advent2020.d4;

List<String> list = new File(getClass().getResource('input.txt').toURI()).collect {it}

int correctNumber = 0

int size = list.size()

def requiredFields = [
    byr: '19[2-9][0-9]|200[0-2]',
    iyr: '201[0-9]|2020',
    eyr: '202[0-9]|2030',
    hgt: '(1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in',
    hcl: '#[0-9a-f]{6}',
    ecl: 'amb|blu|brn|gry|grn|hzl|oth',
    pid: '[0-9]{9}'
]
HashMap<String, String> fields = new HashMap();
for(int i=0; i<size; i++) {
    boolean blankFlag = list[i].isBlank()

    if(!blankFlag) (list[i] =~ /([a-z]{3}):([^ ]+)/).each { fields.put(it[1], it[2] ) }

    if((blankFlag || i == size-1) && fields.keySet().containsAll(requiredFields.keySet())) {
        boolean correct = true
        for(entry in requiredFields) if(!(correct = fields[entry.key] =~ /^${entry.value}$/)) break
            if(correct) correctNumber++
    }

    if(blankFlag) fields.clear()
}

println correctNumber
