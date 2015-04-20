__author__ = 'Victor'

import csv, re

#cleanRegex = r'_\\.*|^"? *\| *| *\| *"?$| *-- *(?=\|)|_\. *|(?<=\d)st (?=\|)|(?<=\d)nd (?=\|)|(?<=\d)rd (?=\|)|(?<=\d)th (?=\|)|(?<=\|) \+|(/\+\d*)+ *|(?<=\|) *| *(?=\|)'
cleanRegex = r'_\\.*|^"? *\| *| *\| *_?\.?"?$| *-- *(?=\|)|_\. *|(?<=\d)st (?=\|)|(?<=\d)nd (?=\|)|(?<=\d)rd (?=\|)|(?<=\d)th (?=\|)|(?<=\|) \+|(/\+\d*)+ *|(?<=\|) *| *(?=\|)'

babMap = [None, 'POOR', 'AVERAGE', 'GOOD']
resistMap = [None, 'POOR', None, 'GOOD']


class ClassAdv:
    def __init__(self):
        self.bab = None
        self.fort = None
        self.refl = None
        self.will = None
        self.traits = [None] * 20


def cleanTable(tableStr):
    pattern = re.compile(cleanRegex, re.MULTILINE)
    result = pattern.sub('', tableStr)

    result = result.splitlines()
    isNotBlankLine = lambda x: not re.match(r'^\s*$', x)
    result = filter(isNotBlankLine, result)
    return list(result)


def readAdvancementRow(row, classAdv):
    level = int(row['Level'])
    if (level == 1):
        classAdv.fort = resistMap[int(row['Fort'])]
        classAdv.refl = resistMap[int(row['Ref'])]
        classAdv.will = resistMap[int(row['Will'])]
    if (level == 3):
        classAdv.bab = babMap[int(row['BAB'])]
    if('Special' in row):
        traitStr = row['Special']
        traitList = re.split(r', *', traitStr)
        classAdv.traits[level - 1] = traitList


def readAdvanementTable(tableLines):
    classAdv = ClassAdv()
    reader = csv.DictReader(tableLines, delimiter='|', quoting=csv.QUOTE_NONE)
    failCount = 0
    for row in reader:
        try:
            readAdvancementRow(row, classAdv)
        except Exception as e:
            failCount += 1
            #print('Bad row: ', row)

    return classAdv, failCount


def readAdvancementStringInRow(row):
    tableStr = row['advancement']
    tableLines = cleanTable(tableStr)
    result, fails = readAdvanementTable(tableLines)
    if (fails > 1):
        print(row['id'], row['name'], 'fails:', fails)
    return result, fails


def addAdvColsToCsv(inFile, outFile):
    with open(inFile, 'r') as inf, open(outFile, 'w') as outf:
        reader = csv.DictReader(inf, delimiter=';', quotechar='"')
        colnames = reader.fieldnames + ['bab', 'fort', 'refl', 'will', 'traits']
        writer = csv.DictWriter(outf, colnames, delimiter=';', quotechar='"', lineterminator='\n')
        writer.writeheader()
        failMap = dict(oneLine=0, manyLines=0)
        for node, row in enumerate(reader, 1):
            adv, rowFails = readAdvancementStringInRow(row)
            if (rowFails > 1):
                failMap['manyLines'] += 1
            elif (rowFails > 0):
                failMap['oneLine'] += 1

            row = dict(row, bab=adv.bab, fort=adv.fort, refl=adv.refl, will=adv.will, traits=adv.traits)
            writer.writerow(row)
        print('failed one line:', failMap['oneLine'], 'many lines:', failMap['manyLines'])


dir = '../../app/src/main/assets/data/csv/'
addAdvColsToCsv(dir + 'classes.csv', dir + 'classes_new.csv')