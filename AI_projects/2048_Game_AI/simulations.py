import pandas as pd  # Importing pandas for Excel operations
import logic
import constants as c
import AI_heuristics as h

class FastGameSim:
    def __init__(self, num_games=100):
        self.num_games = num_games
        self.scores = []
        self.commands = {
            c.KEY_UP: logic.up,
            c.KEY_DOWN: logic.down,
            c.KEY_LEFT: logic.left,
            c.KEY_RIGHT: logic.right,
        }

    def dynamic_depth(self, max_tile):
        """Määrittää dynaamisen syvyyden suurimman laatan perusteella."""
        if max_tile < 131072:
            return 3
        else:
            return 4    

    def expectimax(self, matrix, depth, is_maximizing, alpha=-float('inf'), beta=float('inf')):
        if depth == 0 or logic.game_state(matrix) in ['win', 'lose']:
            return h.heuristic(matrix)

        if is_maximizing:
            best_score = -float('inf')
            for key in [c.KEY_UP, c.KEY_DOWN, c.KEY_LEFT, c.KEY_RIGHT]:
                new_matrix, done = self.commands[key](matrix)[:2]  # Simulate move
                if done:
                    score = self.expectimax(new_matrix, depth - 1, False, alpha, beta)
                    best_score = max(best_score, score)
                    alpha = max(alpha, best_score)
                    if beta <= alpha:
                        break  # Beta cut-off
            return best_score
        else:
            empty_cells = [(i, j) for i in range(c.GRID_LEN) for j in range(c.GRID_LEN) if matrix[i][j] == 0]
            if not empty_cells:
                return h.heuristic(matrix)

            expected_value = 0
            for i, j in empty_cells:
                new_matrix_2 = [row[:] for row in matrix]
                new_matrix_4 = [row[:] for row in matrix]
                new_matrix_2[i][j] = 2
                new_matrix_4[i][j] = 4

                score_2 = self.expectimax(new_matrix_2, depth - 1, True, alpha, beta)
                score_4 = self.expectimax(new_matrix_4, depth - 1, True, alpha, beta)

                expected_value += 0.9 * score_2 + 0.1 * score_4

            return expected_value / len(empty_cells)

    def AI_play(self, matrix):
        max_tile = max(max(row) for row in matrix)  # Get the maximum tile value
        depth = self.dynamic_depth(max_tile)  # Use the new dynamic depth method
        best_move = None
        best_score = -float('inf')
        for key in [c.KEY_UP, c.KEY_DOWN, c.KEY_LEFT, c.KEY_RIGHT]:
            new_matrix, done = self.commands[key](matrix)[:2]  # Simulate move
            if done:
                score = self.expectimax(new_matrix, depth, False)
                if score > best_score:
                    best_score = score
                    best_move = key
        return best_move

    def run_game(self):
        matrix = logic.new_game(c.GRID_LEN)
        points = 0
        while logic.game_state(matrix) != 'lose':
            best_move = self.AI_play(matrix)
            if best_move:
                matrix, done, new_points = self.commands[best_move](matrix)
                points += new_points
                if done:
                    matrix = logic.add_two(matrix)
            else:
                break
        return points

    def run_simulations(self):
        for _ in range(self.num_games):
            score = self.run_game()
            self.scores.append(score)
            print(f"Game {_ + 1}: {score} points")
        self.save_results_to_excel()

    def save_results_to_excel(self):
        """Tallentaa pelitulokset Exceliin ja laskee min, max sekä keskiarvo."""
        df = pd.DataFrame(self.scores, columns=["Score"])  # Create a DataFrame from scores
        df.loc["Min"] = df["Score"].min()
        df.loc["Max"] = df["Score"].max()
        df.loc["Average"] = df["Score"].mean()
        
        # Save the DataFrame to an Excel file
        df.to_excel("2048_Depth3.xlsx", index_label="Game")
        print("Results saved to Excel file.")

# Suorita simulaatiot
fast_game_sim = FastGameSim(num_games=100)
fast_game_sim.run_simulations()
