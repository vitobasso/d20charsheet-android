__author__ = 'Victor'

import csv, re

cleanRegex = r'_\\.*|^"? *\| *| *\| *"?$| *-- *(?=\|)|_\. *|(?<=\d)st (?=\|)|(?<=\d)nd (?=\|)|(?<=\d)rd (?=\|)|(?<=\d)th (?=\|)|(?<=\|) \+|(/\+\d*)+ *|(?<=\|) *| *(?=\|)' #133
# cleanRegex = r'_\\.*|^"? *\| *| *\| *_?\.?"?$| *-- *(?=\|)|_\. *|(?<=\d)st (?=\|)|(?<=\d)nd (?=\|)|(?<=\d)rd (?=\|)|(?<=\d)th (?=\|)|(?<=\|) \+|(/\+\d*)+ *|(?<=\|) *| *(?=\|)' #133
# cleanRegex = r'^"? *\| *| *\| *"?$| *-- *(?=\|)|_\. *|(?<=\d)st *(?=\|)|(?<=\d)nd *(?=\|)|(?<=\d)rd *(?=\|)|(?<=\d)th *(?=\|)|(?<=\|) \+|(/\+\d*)+ *|(?<=\|) *| *(?=\|)|_\\.*$' #133
# cleanRegex = r'_\\.*|^"? *\| *| *\| *_?\.?"?$| *-- *(?=\|)|_\. *|_/\d\. *|(?<=\d)st *(?=\|)|(?<=\d)nd *(?=\|)|(?<=\d)rd *(?=\|)|(?<=\d)th *(?=\|)|(?<=\|) *\+|(/\+\d*)+ *|(?<=\|) *| *(?=\|)' #133

babMap = [None, 'POOR', 'AVERAGE', 'GOOD']
resistMap = ['POOR', None, 'GOOD']


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


def readRow(row):
    advStr = row['advancement']
    advLines = cleanTable(advStr)
    result, fails = readAdvanementTable(advLines)
    if (fails > 1):
        print(row['id'], row['name'], 'fails:', fails)
    return result, fails


def convertHeaders(oldHeaders):
    newHeaders = oldHeaders[:]
    pos = newHeaders.index('advancement')
    newHeaders.remove('advancement')
    newHeaders[pos:pos] = ['bab', 'fort', 'refl', 'will', 'traits']
    return newHeaders


def convertRow(row, failCount):
    adv, rowFails = readRow(row)
    if (rowFails > 1):
        failCount['manyLines'] += 1
    elif (rowFails > 0):
        failCount['oneLine'] += 1
    newRow = dict(row, bab=adv.bab, fort=adv.fort, refl=adv.refl, will=adv.will, traits=adv.traits)
    newRow.pop('advancement')
    return newRow


def convertCsv(inFile, outFile):
    with open(inFile, 'r') as inf, open(outFile, 'w') as outf:
        reader = csv.DictReader(inf, delimiter=';', quotechar='"')
        newHeaders = convertHeaders(reader.fieldnames)
        writer = csv.DictWriter(outf, newHeaders, delimiter=';', quotechar='"', lineterminator='\n')
        writer.writeheader()
        failCount = dict(oneLine=0, manyLines=0)
        for row in reader:
            newRow = convertRow(row, failCount)
            writer.writerow(newRow)
        print('failed one line:', failCount['oneLine'], 'many lines:', failCount['manyLines'])


dir = '../../app/src/main/assets/data/csv/'
convertCsv(dir + 'classes.csv', dir + 'classes_new.csv')