def redistribute_state(state):
    new_state = list(state)
    number_to_redistribute = max(state)
    start_index = state.index(number_to_redistribute)
    new_state[start_index] = 0
    for i in range(1, number_to_redistribute + 1):
        idx = (start_index + i) % len(state)
        new_state[idx] += 1
    return tuple(new_state)

def solve(initial_state):
    observed_states = {initial_state}
    state = initial_state
    moves = 0
    while True:
        state = redistribute_state(state)
        moves += 1
        if state in observed_states:
            return state, moves
        observed_states.add(state)

test_state = (0, 2, 7, 0)
initial_state = (11, 11, 13, 7,	0, 15, 5, 5, 4, 4, 1, 1, 7, 1, 15, 11)
repeated_state = (1, 0, 14, 14, 12, 12, 10, 10, 8, 8, 6, 6, 4, 3, 2, 1)



