import numpy as np
from typing import List, Tuple
import heapq
from scipy.interpolate import splprep, splev


def A_star(map: np.ndarray, start: Tuple, goal: Tuple) -> Tuple[np.ndarray, float]:
    """A* algorithm for path planning.
    
    Args:
        map: 2D numpy array representing the map.
        start: tuple representing the start point.
        goal: tuple representing the goal point.
        
    Returns:
        List[Tuple]: list of points representing the path.
    """
    
    SQRT2 = np.sqrt(2)
    rows, cols = map.shape
    
    def heuristic(a: Tuple, b: Tuple, method: str = 'manhattan') -> float:
        """Calculate heuristic cost between two points."""
        dx, dy = abs(a[0] - b[0]), abs(a[1] - b[1])
        if method == 'manhattan':
            return dx + dy
        elif method == 'euclidean':
            return np.linalg.norm(np.array(a) - np.array(b))
        elif method == 'chebyshev':
            return max(dx, dy)
        else:
            raise ValueError("Invalid heuristic method.")
    
    def get_neighbors(point, num_directions=4):
        """Get neighbors of a point."""
        x, y = point
        neighbors = []
        
        available_directions = [(-1, 0, 1), (1, 0, 1), (0, -1, 1), (0, 1, 1), 
                                (-1, -1, SQRT2), (-1, 1, SQRT2), 
                                (1, -1, SQRT2), (1, 1, SQRT2)]
        if num_directions == 4:
            available_directions = available_directions[:4]
        elif num_directions != 8:
            raise ValueError("num_directions must be 4 or 8.")
        
        # Note: x is column, y is row
        for dy, dx, cost in available_directions:
            nx, ny = x + dx, y + dy
            if 0 <= nx < cols and 0 <= ny < rows and map[ny, nx] == 1:
                neighbors.append((nx, ny, cost))
        return neighbors
    
    open_list = []
    heapq.heappush(open_list, (0, start))
    came_from = {}
    
    # f = g + h
    g_score = {start: 0}  # cost from start to node
    f_score = {start: heuristic(start, goal, method='euclidean')}  # cost from start to goal through node
    
    while open_list:
        _, current = heapq.heappop(open_list)
        
        if current == goal:
            path = []
            while current in came_from:
                path.append(current)
                current = came_from[current]
            path.append(start)
            return np.array(path[::-1]), g_score[goal]
        
        for nx, ny, cost in get_neighbors(current, 8):
            neighbor = (nx, ny)
            # cost = np.linalg.norm(np.array(current) - np.array(neighbor))  # too slow
            tentative_g_score = g_score[current] + cost
            if tentative_g_score < g_score.get(neighbor, float('inf')):
                came_from[neighbor] = current
                g_score[neighbor] = tentative_g_score
                f_score[neighbor] = tentative_g_score + heuristic(neighbor, goal, method='euclidean')
                heapq.heappush(open_list, (f_score[neighbor], neighbor))
    
    return None, None
    
    
def smooth_path(path: np.ndarray) -> Tuple[np.ndarray, np.ndarray]:
    tck, u = splprep(path.T, s=0) 
    u_new = np.linspace(0, 1, 100) 
    x_new, y_new = splev(u_new, tck)
    return x_new, y_new
