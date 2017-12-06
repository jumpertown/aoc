"""Breadth first solver for game definitions."""
class BreadthFirstSolver(object):
    def show_progress(self, num_moves, num_states):
        """Show progress whilst running."""
        print("Move {}: examining {} position{}.".format(
            num_moves,
            num_states,
            "s" if num_states == 1 else ""
        ))

    def solve(self, game):
        """Solve the game returning a set of shortest solutions."""
        observed_states = set()
        # Set of tuples of (previous_moves_tuple, game_state)
        move_states = {((), game.initial_state)}
        num_moves = 0
        while True:
            self.show_progress(num_moves, len(move_states))
            # Check for solutions
            solutions = {m for m, s in move_states if game.is_end_state(s)}
            if solutions:
                return solutions

            # Find the next level of states
            move_states = {
                (prev_moves + (move,), game.next_state(state, move))
                for (prev_moves, state) in move_states
                for move in game.next_moves(state)
            }

            # TODO - remove duplicates for unordered games

            # Prune and update previously observed states
            # (must have been part of a shorter solution)
            move_states = {
                (moves, state) for (moves, state) in move_states
                if state not in observed_states
            }
            if not move_states:
                #No possible next states == no possible solutions
                return set()
            observed_states.update([s for _, s in move_states])
            num_moves += 1


__all__ = (
    'BreadthFirstSolver',
)
