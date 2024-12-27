import cv2
import numpy as np
import matplotlib.pyplot as plt
from planning import A_star
from collections import deque
from scipy.interpolate import splprep, splev
from typing import Tuple


def nearest_passable_point(map: np.ndarray, start: Tuple[int, int]) -> Tuple[int, int]:
    rows, cols = map.shape
    start_x, start_y = start

    # Make sure the start point is within the map
    if start_x < 0:
        start_x = 0
    elif start_x >= cols:
        start_x = cols - 1  
    if start_y < 0:
        start_y = 0
    elif start_y >= rows:
        start_y = rows - 1
        
    # Check if the start point is already passable
    if map[start_y, start_x] == 1:
        return start

    # Initialize the queue and visited set
    queue = deque([(start_x, start_y)])
    visited = set((start_x, start_y))

    # Define four directions (up, down, left, right)
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

    # Start breadth-first search
    while queue:
        x, y = queue.popleft()

        for dx, dy in directions:
            nx, ny = x + dx, y + dy

            if 0 <= nx < cols and 0 <= ny < rows and (nx, ny) not in visited:
                if map[ny, nx] == 1:
                    return (nx, ny)

                queue.append((nx, ny))
                visited.add((nx, ny))

    return None


def read_map(image_path) -> np.ndarray:
    image = cv2.imread(image_path)
    image = np.array(cv2.cvtColor(image, cv2.COLOR_BGR2GRAY))

    threashold = 255 // 2
    map = image.copy()
    map[map < threashold] = 1  # black is path
    map[map >= threashold] = 0  # white is wall

    return map.astype(np.uint8)


def downsample_map(map, scale=2):
    # if all elements in a block are 1, then the block is 1, otherwise 0
    h, w = map.shape
    downsampled_map = np.zeros((h // scale, w // scale), dtype=np.uint8)
    
    for i in range(0, h, scale):
        for j in range(0, w, scale):
            # if the block is out of the map, skip
            if i + scale > h or j + scale > w:
                continue
            block = map[i:i+scale, j:j+scale]
            if np.all(block == 1):
                downsampled_map[i // scale, j // scale] = 1
    
    return downsampled_map


def get_map_image(map, free_space=None):
    # 1: gray, 0: white
    map_image = np.zeros((map.shape[0], map.shape[1], 3), dtype=np.uint8)
    map_image[map == 1] = [150, 150, 150]
    map_image[map == 0] = [200, 200, 200]
    if free_space is not None:
        map_image[free_space == 1] = [50, 50, 50]
    return map_image


def get_free_space(map, size=5):
    # A turning car can be represented as a circle with radius size
    kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (size, size))
    free_space = cv2.erode(map, kernel)
    return free_space


def plot_map(map):
    map_image = get_map_image(map)
    plt.imshow(map_image, interpolation='nearest')
    plt.show()
    

def plot_free_space(map, free_space):
    map_image = get_map_image(map, free_space)
    plt.imshow(map_image, interpolation='nearest')
    plt.show()
    

def draw_click(map, free_space=None, num_clicks=2):
    map_image = get_map_image(map)
    # plt.imshow(map_image, interpolation='nearest')
    plot_free_space(map, free_space)
    
    xy = plt.ginput(num_clicks)
    index = [(int(np.floor(x)), int(np.floor(y))) for x, y in xy]
    
    # mark the clicked points with a cross
    for x, y in xy:
        plt.plot(x, y, 'rx', markersize=10)
        
    print("Clicked index:", index)
    # plt.show()
    return index


if __name__ == '__main__':
    map = read_map('../assets/maps/map.bmp')    
    assert np.all(np.isin(map, [0, 1])), "Map must contain only 0 and 1."
    
    map_image = get_map_image(map)
    plt.figure(1)
    plt.imshow(map_image, interpolation='nearest')
    plt.figure(2)
    scale = 8
    free_space = get_free_space(map, size=15)
    map = downsample_map(map, scale)
    free_space = downsample_map(free_space, scale)
    
    plt.ion()
    
    start, goal = draw_click(map, free_space)
    # make sure the start and goal points are passable
    start = nearest_passable_point(free_space, start)
    goal = nearest_passable_point(free_space, goal)
    print("Start:", start, "Goal:", goal)
        
    # plot_map(map)
    path, cost = A_star(free_space, start, goal)
    print("Cost:", cost)
    
    plt.figure(2)
    plt.plot([x for x, y in path], [y for x, y in path], 'r-')
    plt.draw()
    
    interval = 4
    sampled_path = np.array(path[::interval])
    plt.figure(1)
    tck, u = splprep(sampled_path.T, s=0) 
    u_new = np.linspace(0, 1, 100) 
    x_new, y_new = splev(u_new, tck)
    # plt.plot([x * scale for x, y in sampled_path], [y * scale for x, y in sampled_path], 'r-')
    plt.plot(x_new * scale, y_new * scale, 'r-', linewidth=2)
    plt.draw()
    plt.pause(0.001)
    
    plt.ioff()
    plt.show()
    
    # plot_free_space(map, free_space)
    
    print("Path:", path)
