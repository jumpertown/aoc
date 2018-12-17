"""Breadth first solver for game definitions."""
class BreadthFirstSolver(object):
    def show_progress(self, num_moves, num_states):
        """Show progress whilst running."""
        print("Move {}: examining {} position{}.".format(
            num_moves,
            num_states,
            "s" if num_states == 1 else ""
        ))

    def solve(self, game, max_moves=0):
        """Solve the game returning a set of shortest solutions."""
        observed_states = set()
        # Set of tuples of (previous_moves_tuple, game_state)
        move_states = {((), game.initial_state)}
        num_moves = 0
        best_state = game.initial_state
        best_score = game.score(best_state)
        while True:
            # self.show_progress(num_moves, len(move_states))
            if num_moves > max_moves:
                raise BufferError

            # Check for solutions
            solutions = {m for m, s in move_states if game.is_end_state(s)}
            if solutions:
                return solutions

            # Check if we've found a better state:
            for _, s in move_states:
                if game.score(s) > best_score:
                    best_state = s
                    best_score = game.score(s)
                    print('High Score: ', best_score)
                
                # Hacked in to solve puzzle
                if num_moves == 40:
                    print ("Move 40: ", game.score(s)) 

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
