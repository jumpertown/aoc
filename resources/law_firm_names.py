"""Example game: Fewest law firms using all letters."""

import os
from .game import Game

# A-Z ASCII 65-91
LETTERS = [chr(x) for x in range(65, 91)]

class LawFirmNamesGame(Game):

    def __init__(self, use_heuristic=True):
        """Initialisation.
        
        use_heuristic: Whether to predict best next moves
        """
        self.use_heuristic = use_heuristic

        # Load law firms
        d = os.path.dirname(os.path.abspath(__file__))
        file_path = os.path.join(d, "law_firms.txt")
        with open(file_path) as fh:
            law_firms = fh.readlines()
        self.law_firms = [l.strip() for l in law_firms]

    @property
    def initial_state(self):
        """The initial state of the game."""
        return ()

    @property
    def ordered_moves(self):
        """Do the moves in the game have an order."""
        return False

    def next_moves(self, state):
        """Set of possible next moves from this `state`."""
        remaining_firms = [l for l in self.law_firms if l not in state]
        if self.use_heuristic:
            return set(
                self.firms_using_least_frequently_used_letter(
                    remaining_firms,
                    self.remaining_letters(state)
                )
            )
        return set(remaining_firms)

    def next_state(self, state, move):
        """Next state of the system given move `move` in state `state`."""
        return state + (move,)

    def is_end_state(self, state):
        """Is the current state `state` an end state of the game."""
        return not self.remaining_letters(state)

    def remaining_letters(self, state):
        used_letters = ''.join(state)
        return [l for l in LETTERS if used_letters.find(l) == -1]

    def firms_using_letter(self, firms, letter):
        return [f for f in firms if f.find(letter) != -1]

    def firms_using_least_frequently_used_letter(self, firms, letters):
        letter_firms = [self.firms_using_letter(firms, l) for l in letters]
        letter_firms.sort(key=lambda f: len(f))
        return letter_firms[0] if letter_firms else []


__all__ = (
    'LawFirmNamesGame',
)

