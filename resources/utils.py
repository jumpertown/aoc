def get_puzzle_input(file):
    """Return the puzzle input as a list."""
    with open(file) as fh:
        lines = fh.readlines()
    return [l.strip() for l in lines if l.strip()]


def sum_indices(*items):
    """Sum each index of the ordered collections, returing a tuple sum.

    sum_tuple((1, 2), (3, 4), (5, 6))
    -> (9, 12)
    """
    return tuple(sum(line) for line in zip(*items))


assert sum_indices((1, 2), (3, 4), (5, 6)) == (9, 12)


