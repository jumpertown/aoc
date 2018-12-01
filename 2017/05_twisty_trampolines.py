test_jumps = [0, 3, 0, 1, -3]

with open('jumps_input.txt') as fh:
    jumps = fh.readlines()
jumps = [int(j.strip()) for j in jumps]

def jump_steps(jumps):
    moves = 0
    pos = 0   
    while True:
        val = jumps[pos]
        if val >= 3:
            jumps[pos] -= 1
        else:
            jumps[pos] += 1
        pos += val
        moves += 1
        if pos < 0 or pos >= len(jumps):
            return moves

print(jump_steps(list(jumps)))


