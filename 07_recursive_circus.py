import re

# pbga(66)
# xhth(57)
# ebii(61)
# havc(66)
# ktlj(57)
# fwft(72) -> ktlj, cntj, xhth
# qoyq(66)
# padx(45) -> pbga, havc, qoyq
# tknk(41) -> ugml, padx, fwft
# jptl(61)
# ugml(68) -> gyxo, ebii, jptl
# gyxo(61)
# cntj(57)
programs = [
    ('pbga', 66, []),
    ('xhth', 57, []),
    ('ebii', 61, []),
    ('havc', 66, []),
    ('ktlj', 57, []),
    ('fwft', 72, ['ktlj', 'cntj', 'xhth']),
    ('qoyq', 66, []),
    ('padx', 45, ['pbga', 'havc', 'qoyq']),
    ('tknk', 41, ['ugml', 'padx', 'fwft']),
    ('jptl', 61, []),
    ('ugml', 68, ['gyxo', 'ebii', 'jptl']),
    ('gyxo', 61, []),
    ('cntj', 57, []),
]

# ifyzcgi (14)
# axjvvur (50)
# tcmdaji (40) -> wjbdxln, amtqhf
regexp = r'^([a-z]+) \(([0-9]+)\)(?: -> ([a-z, ]+))?$'

with open('circus.txt') as fh:
    lines = fh.readlines()
    lines = [l.strip() for l in lines]

programs = []
for l in lines:
    children = []
    m = re.match(regexp, l)
    if not m:
        continue
    groups = m.groups()
    if groups[2]:
        children = groups[2].split(',')
        children = [c.strip() for c in children]
    programs.append((groups[0], int(groups[1]), children))

programs_dict = {n: (w, c) for (n, w, c) in programs}
def get_root_nodes(programs):
    names = [n for (n,_,_) in programs]
    l = [c for (_, _, c) in programs]
    children = [item for sublist in l for item in sublist]
    return [n for n in names if n not in children]

def get_node_weight(node):
    w, children = programs_dict[node]
    return w + sum([get_node_weight(c) for c in children])

for name, _, children in programs:
    weights = [get_node_weight(c) for c in children]
    unique_weights = set(weights)
    if len(unique_weights) > 1:
        print(name, children, weights)




