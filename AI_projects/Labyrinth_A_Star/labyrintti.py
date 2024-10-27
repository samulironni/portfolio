import matplotlib.pyplot as plt
import numpy as np
import matplotlib.patches as patches
from matplotlib.animation import FuncAnimation

class Node:
    """Solmu-luokka A*-reittihakuun"""
    
    def __init__(self, parent=None, position=None):
        self.parent = parent # Edellinen solmu polulla
        self.position = position # Nykyisen solmun koordinaatit
        self.g = 0  # Kustannus aloituspaikasta nykyiseen solmuun
        self.h = 0 # Arvioitu kustannus tästä solmusta maaliin
        self.f = 0 # Kokonaiskustannus (g + h)

    def __eq__(self, other):
        return self.position == other.position

    def __repr__(self):
        return f"Node(position={self.position}, f={self.f}, g={self.g}, h={self.h})"

# Suunnat ja niiden koordinaattimuutokset
UP, RIGHT, DOWN, LEFT = (-1, 0), (0, 1), (1, 0), (0, -1)
DIRECTION_NAMES = {UP: "YLÖS", RIGHT: "OIKEA", DOWN: "ALAS", LEFT: "VASEN"}

# Sallitut liikkeet eri suuntiin (menosuunnassa eteenpäin ja oikealle)
Allowed_Moves_for_Direction = {
    UP: [UP, RIGHT],
    RIGHT: [RIGHT, DOWN],
    DOWN: [DOWN, LEFT],
    LEFT: [LEFT, UP]
}

def heuristic(a, b):
    """Heuristiikkafunktio: Euclidean-etäisyys^2"""
    return (((a[0] - b[0])**2) + ((a[1] - b[1])**2)) 
    
def astar(maze, start, end):
    """Palauttaa listan tuplista poluksi annetun start- ja end-pisteen välillä annettua labyrinttiaukkoa käyttäen"""
    
    # Luodaan aloitus- ja päätepisteen solmut
    start_node = Node(None, start)
    end_node = Node(None, end)
    open_list = [] # Käsittelemättömät solmut
    open_list.append(start_node)
    closed_set = set() # Käsitellyt solmut
    came_from = {} # Tallentaa mistä solmusta kuhunkin solmuun tultiin
    g_cost = {} # Tallentaa kustannuksen aloitussolmusta kuhunkin solmuun
    
    came_from[start_node.position] = None
    g_cost[start_node.position] = 0

    while open_list:
        # Valitaan pienimmän f-arvon omaava solmu 
        open_list.sort(key=lambda node: node.f)
        current_node = open_list.pop(0)
        closed_set.add(current_node.position)

        # Jos ollaan päätepisteessä, palautetaan reitti
        if current_node == end_node:
            path = []
            directions = []
            while current_node is not None:
                path.append(current_node.position)
                if current_node.parent is not None:
                    move = (current_node.position[0] - current_node.parent.position[0],
                            current_node.position[1] - current_node.parent.position[1])
                    direction_str = DIRECTION_NAMES.get(move, "TUNNISTAMATON")
                    directions.append(direction_str)
                current_node = current_node.parent
            return simplify_path(path[::-1]), directions[::-1]

        # Määritetään sallitut liikkeet
        allowed_moves = [UP, RIGHT, DOWN, LEFT]
        if current_node.parent is not None:
            move = (current_node.position[0] - current_node.parent.position[0],
                    current_node.position[1] - current_node.parent.position[1])
            allowed_moves = Allowed_Moves_for_Direction.get(move, allowed_moves)

        # Tarkistetaan mahdolliset liikkeet
        for move in allowed_moves:
            node_position = (current_node.position[0] + move[0], current_node.position[1] + move[1])
            
            # Tarkistetaan ettei liikuta labyrintin ulkopuolelle
            if (node_position[0] >= len(maze) or node_position[0] < 0 or
                node_position[1] >= len(maze[0]) or node_position[1] < 0):
                continue
            
            # Tarkistetaan ettei törmätä esteeseen
            if maze[node_position[0]][node_position[1]] != 0:
                continue
            
            # Luodaan uusi solmu
            new_node = Node(current_node, node_position)
            
            # Tarkistetaan onko solmu jo käsitelty
            if new_node.position in closed_set and g_cost.get(new_node.position, float('inf')) <= new_node.g:
                continue
            
            # Päivitetään solmun kustannukset ja heuristiikka
            new_node.g = current_node.g + 1
            new_node.h = heuristic(new_node.position, end_node.position)
            new_node.f = new_node.g + new_node.h

            # Lisätään solmu avoimeen listaan
            if any(new_node == open_node and new_node.f >= open_node.f for open_node in open_list):
                continue

            open_list.append(new_node)
            came_from[new_node.position] = current_node.position
            g_cost[new_node.position] = new_node.g

    return [], [] # Palautetaan tyhjät listat, jos reittiä ei löydy

def simplify_path(path):
    """Poistaa turhat käännökset polusta"""
    if not path:
        return []

    new_path = [path[0]]
    for i in range(1, len(path) - 1):
        prev = new_path[-1]
        curr = path[i]
        next = path[i + 1]

        # Tarkistetaan onko suunta muuttunut
        if (curr[0] - prev[0], curr[1] - prev[1]) != (next[0] - curr[0], next[1] - curr[1]):
            new_path.append(curr)

    new_path.append(path[-1])
    return new_path

def visualize_maze_and_path(maze, path):
    """Visualisoi labyrintin ja reitin reaaliaikaisena animaationa matplotlibin avulla"""

    def add_arrow(ax, start, direction, color='black'):
        """Lisää nuoli kuvioon."""
        arrow = patches.FancyArrowPatch(
            (start[1], start[0]),  # Alkuperäinen solmu
            (start[1] + 0.5 * direction[1], start[0] + 0.5 * direction[0]),  # Nuolen pää
            mutation_scale=20,
            color=color,
            arrowstyle='->',
            linewidth=3
        )
        return arrow

    # Muunna labyrintti numpy-taulukoksi
    maze_array = np.array(maze)

    # Luo kuva ja akselit
    fig, ax = plt.subplots(figsize=(10, 10))
    
    # Näytä labyrintti
    im = ax.imshow(maze_array, cmap='gray_r', origin='upper')

    # Luo reitin animaatio
    path_x, path_y = zip(*path)
    path_x = list(path_x)
    path_y = list(path_y)

    # Luodaan tyhjät listat animaatiota varten
    path_line, = ax.plot([], [], 'o-', color='red', markersize=5, linestyle='-', linewidth=2)
    current_point, = ax.plot([], [], 'o', color='blue', markersize=10)
    arrows = []

    def init():
        path_line.set_data([], [])
        current_point.set_data([], [])
        for arrow in arrows:
            arrow.remove()
        arrows.clear()
        return path_line, current_point

    def update(frame):
        # Päivitä reitin osuus
        path_line.set_data(path_y[:frame], path_x[:frame])
        # Päivitä nykyinen kohta
        current_point.set_data(path_y[frame], path_x[frame])
        
        # Lisää nuolia käännöksiin
        for arrow in arrows:
            arrow.remove()
        arrows.clear()
        
        if frame > 1:
            prev = (path_x[frame - 2], path_y[frame - 2])
            current = (path_x[frame - 1], path_y[frame - 1])
            next = (path_x[frame], path_y[frame])
            
            direction = (next[0] - current[0], next[1] - current[1])
            prev_direction = (current[0] - prev[0], current[1] - prev[1])
            
            if direction != prev_direction:
                # Lisää nuoli käännökseen
                arrow = add_arrow(ax, current, direction, color='blue')
                arrows.append(arrow)
                ax.add_patch(arrow)
                
        return path_line, current_point

    ani = FuncAnimation(fig, update, frames=len(path_x), init_func=init, blit=True, interval=250)

    plt.grid(which='both', color='black', linestyle='-', linewidth=1)
    plt.title('Labyrintti ja reitin eteneminen')
    plt.show()

def main():
    """Pääohjelma, jossa määritellään labyrintti.
        0 = avoin solmu, 1 = este"""
    
    maze1 = [[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
             [0, 1, 0, 1, 0 ,1, 1, 1, 0, 1, 1, 1, 0],
             [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
             [0, 1, 0, 1, 0 ,1, 1, 1, 1, 1, 0, 1, 0],
             [0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0],
             [0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0],
             [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0],
             [1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0],
             [1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0],
             [1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0],
             [1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
             [1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1]]

    start1 = (11, 10)
    end1 = (11, 2)

    # Käytetään A*-algoritmia reitin etsimiseen
    path, directions = astar(maze1, start1, end1)
    print("Löydetty reitti:", path)
    print("Suunnat, joihin on liikuttu:", directions)

    # Visualisoi labyrintti ja reitti
    visualize_maze_and_path(maze1, path)

# Suoritetaan pääohjelma
if __name__ == '__main__':
    main()
