def max_for_layers():
    layer = 0
    num_in_layer = 1
    cur_max = 1
    yield cur_max
    num_in_layer = 8
    cur_max = 9
    yield cur_max
    while True:
        num_in_layer += 8
        cur_max += num_in_layer
        yield cur_max


def which_layer(num):
    layer = 0
    gen = max_for_layers()
    prev_max = 0
    while True:
        max = next(gen)
        if num <= max:
            break
        prev_max = max
        layer += 1
    return (layer, prev_max + 1)


def length_side_in_layer(layer):
    return 2 * layer + 1


def position(num):
    if num == 1:
        return (0,0)

    layer, first_num_in_layer = which_layer(num)
    sides = length_side_in_layer(layer)
    initial_pos = (layer, 1 - layer)

    first_num = first_num_in_layer
    # Starts one up from the bottom
    # Go up
    if num <= first_num + sides - 2:
        y_start = 1 - layer
        y_pos = num - first_num + (1 - layer)
        return (layer, y_pos)
    # Go left
    first_num += sides - 1
    if num <= first_num + sides - 2:
        x_pos = layer - 1 - (num - first_num)
        return (x_pos, layer)
    # Go down   
    first_num += sides - 1
    if num <= first_num + sides - 2:
        y_pos = layer - 1 - (num - first_num)
        return (-layer, y_pos)
    # Go right
    first_num += sides - 1
    x_pos = num - first_num + (1 - layer)
    return (x_pos, -layer)


def position_to_layer(pos):
    x,y = pos
    layer = max(abs(x), abs(y))
    return layer


def distance(num):
    x, y = position(num)
    return abs(x) + abs(y)


# Create reverse lookup
positions = dict()
positions[(0,0)] = 1
for i in range(2, 325489):
    positions[position(i)] = i


def get_neighbours(pos):
    x,y = pos
    return [
        (x-1, y),
        (x+1, y),
        (x-1, y+1),
        (x, y+1),
        (x+1, y+1),
        (x-1, y-1),
        (x, y-1),
        (x+1, y-1)
    ]


def number_neighbours(num):
    pos = position(num)
    neighbours = get_neighbours(pos)
    number_neighbours = [positions[n] for n in neighbours]
    return number_neighbours


def make_sums(up_to_sum):
    sols = [1]
    i = 2
    while True:
        sol = sum([sols[n-1] for n in number_neighbours(i) if n < i])
        sols.append(sol)
        if sol >= up_to_sum:
            return sols
        i += 1

print(make_sums(325489))



    






