import itertools
import numpy as np
import logic
import constants as c
import AI_heuristics as h
from puzzle import GameGrid

class GridSearch2048:
    def __init__(self, game_iterations=5):
        self.game_iterations = game_iterations  # Number of games to run per configuration
        # self.depth = depth  # Depth for the expectimax algorithm
    
    def run_game(self, weights):
        """
        Runs a single game with the given heuristic weights.
        :param weights: A tuple of weights for the heuristic function.
        :return: The total score achieved in the game.
        """
        game = GameGrid()  # Create a new instance of the game
        # h.WEIGHTS = weights  # Set the heuristic weights globally
        
        while not game.game_over:
            key = game.AI_play()  # Get the AI's best move based on the heuristic
            if key:
                game.matrix, done, points = game.commands[key](game.matrix)
                game.points += points
                if done:
                    game.matrix = logic.add_two(game.matrix)
                if logic.game_state(game.matrix) == 'lose':
                    break
        
        return game.points  # Return the score achieved by the game
    
    def grid_search(self, param_grid):
        """
        Performs a grid search over the given parameter grid.
        :param param_grid: Dictionary containing parameter names and their ranges.
        :return: The best parameters and their corresponding average score.
        """
        param_names = list(param_grid.keys())
        param_values = list(param_grid.values())
        best_params = None
        best_score = -float('inf')
        
        # Iterate over all possible combinations of parameters
        for param_combination in itertools.product(*param_values):
            weights = dict(zip(param_names, param_combination))
            print(f"Testing combination: {weights}")
            
            # Run the game for a number of iterations and calculate average score
            scores = []
            for _ in range(self.game_iterations):
                score = self.run_game(weights)
                scores.append(score)
            
            avg_score = np.mean(scores)
            print(f"Average score for {weights}: {avg_score}")
            
            # Update the best parameters if the current one performs better
            if avg_score > best_score:
                best_score = avg_score
                best_params = weights
        
        return best_params, best_score


if __name__ == "__main__":
    # Define the parameter grid for the heuristic function
    param_grid = {
        'smoothness_weight': [0.05, 0.1, 0.2],
        'free_cells_weight': [5, 10, 15],
        'max_tile_in_corner_weight': [50, 100, 200],
        'monotonicity_weight': [0.25, 0.5, 1, 2, 5],
        'merge_potential_weight': [0.5, 1.0, 2.0],
        'snake_heuristic_weight': [0.25, 0.5, 1.0, 1.5, 2.5]
    }

    grid_search = GridSearch2048(game_iterations=5, n_jobs=-1, verbose=1)
    best_params, best_score = grid_search.grid_search(param_grid)
    
    print(f"Best parameters: {best_params}")
    print(f"Best average score: {best_score}")
