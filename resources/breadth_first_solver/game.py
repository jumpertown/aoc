"""Interface for game definitions."""
class Game(object):

    @property
    def initial_state(self):
        """The initial state of the game.
        
        States must be hashable.
        """
        raise NotImplementedError()

    @property
    def ordered_moves(self):
        """Do the moves in the game have an order?

        Is [move 1, move2, move3] != [move2, move3, move1]
        """
        return True

    def next_moves(self, state):
        """Possible set of next moves from this `state`.

        If you're interested in the shortest solution only apply a heuristic
        if you _know_ that other moves cannot lead to a shorter solution. For
        example in the Tube Map Name problem it is fine to suggest 'Belsize Park'
        as a move given it's the only one with a 'Z' in it; hence must be in the
        shortest solution.
        """
        raise NotImplementedError()

    def next_state(self, state, move):
        """Next state of the system given move `move` in state `state`."""
        raise NotImplementedError()

    def is_end_state(self, state):
        """Is the current state `state` an end state of the game."""
        raise NotImplementedError()

    def score(self, state):
        """The score of the current `state`."""
        return 0


__all__ = (
    'Game',
)
