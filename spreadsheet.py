from itertools import combinations

with open('/tmp/puzzle2.txt') as fh:
    lines = fh.readlines()

lines = [l.strip() for l in lines if l.strip()]
sheet = [[int(i) for i in l.split('\t')] for l in lines]

def calc_checksum(sheet):
    sum = 0
    i = 1
    for row in sheet:
        print("{}: len: {}, max: {}, min: {}, diff: {}".format(
            i,
            len(row),
            max(row),
            min(row),
            max(row) - min(row)
        ))
        i += 1
        sum += (max(row) - min(row))
    return sum

def calc_checksum_2(sheet):
    sum = 0
    i = 1
    for row in sheet:
        for combo in combinations(row, 2):
            mx = max(combo)
            mn = min(combo)
            if mx % mn == 0:
                print('{} found: max {} min {} div {}'.format(
                    i,
                    mx,
                    mn,
                    mx // mn
                ))
                sum += (mx // mn)
        i += 1
    return sum

print('Solution 1: {}'.format(calc_checksum(sheet)))
print('Solution 2: {}'.format(calc_checksum_2(sheet)))

