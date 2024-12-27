import requests
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import linear_sum_assignment
import cv2
import threading
import time
from pprint import pprint
from typing import List

from car import Car
from passenger import Passenger
import map_utils
from planning import A_star, smooth_path

MOBILE_CAR_ID = 4
NUM_CARS = 4
NUM_PASSENGERS = 3


class System:
    def __init__(self, engine, ip="localhost", port=8888, visualize=False):
        self.engine = engine
        self.url = f"http://{ip}:{port}"
        self.visualize = visualize
        
        self.map_scale = 8
        self.mobile_car: Car = None
        self.cars: List[Car] = []
        self.car_size = 16
        self.passengers: List[Passenger] = []
        self.update_interval = 0.1
        self.distance_threshold = 3.0

        self.raw_map = self.get_map(online=True)
        self.raw_free_space = map_utils.get_free_space(self.raw_map, self.car_size)

        # downsample the map for faster path planning
        self.car_size = self.car_size // self.map_scale
        self.map = map_utils.downsample_map(self.raw_map, self.map_scale)
        self.free_space = map_utils.downsample_map(self.raw_free_space, self.map_scale)

        info = self.init_backend()
        self._initialize_entities(info)

    def _initialize_entities(self, info):
        self.cars = [None] * NUM_CARS
        self.passengers = [None] * NUM_PASSENGERS

        for car_info in info["cars"]:
            # Make sure the car is placed on a passable point
            x = int(car_info["carX"]) // self.map_scale
            y = int(car_info["carY"]) // self.map_scale
            passable_x, passable_y = map_utils.nearest_passable_point(
                self.free_space, (x, y)
            )

            car = Car(
                engine=self.engine,
                id=car_info["carIndex"],
                x=passable_x,
                y=passable_y,
                theta=np.deg2rad(car_info["carAngle"]),
                dt=self.update_interval,
            )
            print(f"Car {car.id} at ({car.x}, {car.y})")

            if car.id == MOBILE_CAR_ID:
                car.theta = -np.pi / 2  # mobile car faces up
                self.mobile_car = car
            else:
                self.cars[car.id] = car

            # Update the car's position
            self.set_car_info(car)  

        for passenger_info in info["passengers"]:
            # Make sure the passenger's start and end points are passable
            start_x = int(passenger_info["startX"]) // self.map_scale
            start_y = int(passenger_info["startY"]) // self.map_scale
            dest_x = int(passenger_info["endX"]) // self.map_scale
            dest_y = int(passenger_info["endY"]) // self.map_scale
            start_x, start_y = map_utils.nearest_passable_point(
                self.free_space, (start_x, start_y)
            )
            dest_x, dest_y = map_utils.nearest_passable_point(
                self.free_space, (dest_x, dest_y)
            )

            passenger = Passenger(
                id=passenger_info["passengerIndex"],
                start_x=start_x,
                start_y=start_y,
                dest_x=dest_x,
                dest_y=dest_y,
                car_id=passenger_info["nowCarIndex"],
            )
            self.passengers[passenger.id] = passenger

    def init_backend(self):
        url = f"{self.url}/api/init"
        response = requests.get(url)
        return response.json()

    def get_all_cars_info(self):
        url = f"{self.url}/api/car/select/all"
        response = requests.get(url)
        return response.json()

    def get_car_info(self, car_id):
        url = f"{self.url}/api/car/select"
        params = {"carIndex": car_id}
        response = requests.get(url, params=params)
        return response.json()

    def set_car_info(self, car: Car):
        url = f"{self.url}/api/car/update"
        params = {
            "carIndex": car.id,
            "row": car.y * self.map_scale,
            "col": car.x * self.map_scale,
            "angle": np.rad2deg(car.theta),
        }
        response = requests.get(url, params=params)
        return response.text

    def get_all_passengers_info(self):
        url = f"{self.url}/api/passenger/select/all"
        response = requests.get(url)
        return response.json()

    def get_passenger_info(self, passenger_id):
        url = f"{self.url}/api/passenger/select"
        params = {"passengerIndex": passenger_id}
        response = requests.get(url, params=params)
        return response.json()

    def pick_passenger(self, passenger: Passenger):
        url = f"{self.url}/api/passenger/pick"
        params = {"id": passenger.id}
        response = requests.get(url, params=params)
        return response.text

    def drop_passenger(self, passenger: Passenger):
        url = f"{self.url}/api/passenger/drop"
        params = {"id": passenger.id}
        response = requests.get(url, params=params)
        return response.text

    def get_map(self, online=True) -> np.ndarray:
        if online:
            url = f"{self.url}/api/map/select"
            response = requests.get(url)
            map = np.array(response.json(), dtype=np.uint8)
            return map
        else:
            map_path = "../assets/maps/map.bmp"
            map = map_utils.read_map(map_path)
            return map

    def refresh_mobile_car(self):
        while self.mobile_car.status != Car.Status.FINISHED:
            car_info = self.get_car_info(self.mobile_car.id)
            self.mobile_car.x = car_info["carX"] / self.map_scale
            self.mobile_car.y = car_info["carY"] / self.map_scale
            self.mobile_car.theta = np.deg2rad(car_info["carAngle"])
            time.sleep(self.update_interval)

    def get_free_space_with_obstacles(self, car_id):
        # Cars except the given car are obstacles
        free_space = self.free_space.copy()
        obstacle_size = self.car_size
        all_cars = self.cars + [self.mobile_car]

        for car in all_cars:
            if car.id != car_id:
                # Draw a circle centered at the car's position
                x, y = int(car.x), int(car.y)
                cv2.circle(free_space, (x, y), obstacle_size, 0, -1)
        return free_space

    def assign_passengers(self):
        # Hungarian algorithm
        # cost[i, j] = distance between car i and passenger j
        cost = np.zeros((NUM_CARS, NUM_PASSENGERS))
        for i, car in enumerate(self.cars):
            if car.id == MOBILE_CAR_ID:
                cost[i, :] = np.inf
                continue

            free_space_with_obs = self.get_free_space_with_obstacles(car.id)
            # map_utils.plot_free_space(self.map, free_space)

            for j, passenger in enumerate(self.passengers):
                start = (int(car.x), int(car.y))
                goal = (int(passenger.start_x), int(passenger.start_y))
                _, cost_value = A_star(
                    free_space_with_obs,
                    start,
                    goal
                )
                if cost_value is None:
                    cost[i, j] = np.inf
                else:
                    cost[i, j] = cost_value

        row_ind, col_ind = linear_sum_assignment(cost)
        return row_ind, col_ind

    def plot_map_with_entities(self):
        # car_image_folder = "../assets/car"
        # car_name = {
        #     0: "green_car.png",
        #     1: "orange_car.png",
        #     2: "blue_car.png",
        #     3: "yellow_car.png",
        #     4: "red_car.png",
        # }
        
        # plt.ion()
        # cv2.namedWindow("Map with entities", cv2.WINDOW_NORMAL)
        # cv2.resizeWindow("Map with entities", 600, 600)
        map_image = map_utils.get_map_image(self.raw_map)  # , self.raw_free_space)
        # draw circles using cv2
        car_color = (255, 0, 0)
        mobile_car_color = (0, 255, 255)
        passenger_color = (0, 255, 0)
        dest_color = (0, 0, 255)
        radius = self.car_size // 2 * self.map_scale
        all_cars = self.cars + [self.mobile_car]

        for car in all_cars:
            # car_image_path = f"{car_image_folder}/{car_name[car.id]}"
            # car_image = cv2.imread(car_image_path, cv2.IMREAD_UNCHANGED)
            
            cv2.circle(
                map_image,
                (int(car.x * self.map_scale), int(car.y * self.map_scale)),
                radius,
                car_color if car.id != MOBILE_CAR_ID else mobile_car_color,
                -1,
            )
            
            arrow_length = radius * 2
            arrow_end_x = int(car.x * self.map_scale + arrow_length * np.cos(car.theta))
            arrow_end_y = int(car.y * self.map_scale + arrow_length * np.sin(car.theta))
            cv2.arrowedLine(
                map_image,
                (int(car.x * self.map_scale), int(car.y * self.map_scale)),
                (arrow_end_x, arrow_end_y),
                (0, 0, 0),
                2,  # thickness
                tipLength=0.3,
            )
        
        for passenger in self.passengers:
            if passenger.status == Passenger.Status.WAITING:
                cv2.circle(
                    map_image,
                    (
                        int(passenger.start_x * self.map_scale),
                        int(passenger.start_y * self.map_scale),
                    ),
                    radius,
                    passenger_color,
                    -1,
                )
            elif passenger.status == Passenger.Status.PICKED_UP:
                cv2.circle(
                    map_image,
                    (
                        int(passenger.dest_x * self.map_scale),
                        int(passenger.dest_y * self.map_scale),
                    ),
                    radius,
                    dest_color,
                    -1,
                )
        
        map_image = cv2.cvtColor(map_image, cv2.COLOR_RGB2BGR)
        cv2.imshow("Map with entities", map_image)
        cv2.waitKey(1)
        # plt.imshow(map_image)
        # plt.draw()
        # plt.pause(0.01)

    def move_car(self, car: Car):
        while True:
            if car.status == Car.Status.IDLE or car.status == Car.Status.FINISHED:
                break

            start_time = time.time()
            if car.status == Car.Status.PICKING_UP or car.status == Car.Status.DROPPING_OFF:
                free_space_with_obs = self.get_free_space_with_obstacles(car.id)
                passenger: Passenger = self.passengers[car.passenger_id]
                # Note error handling
                start = map_utils.nearest_passable_point(
                    free_space_with_obs, (int(car.x), int(car.y))
                )
                if car.status == Car.Status.PICKING_UP:
                    goal = map_utils.nearest_passable_point(
                        free_space_with_obs, (int(passenger.start_x), int(passenger.start_y))
                    )
                elif car.status == Car.Status.DROPPING_OFF:
                    goal = map_utils.nearest_passable_point(
                        free_space_with_obs, (int(passenger.dest_x), int(passenger.dest_y))
                    )
                distance = np.linalg.norm(np.array(start) - np.array(goal))

                # If the car is close enough to the goal, pick up or drop off the passenger
                if distance < self.distance_threshold:
                    if car.status == Car.Status.PICKING_UP:
                        self.pick_passenger(passenger)
                        car.pick_passenger()
                        passenger.pick_up(car.id)
                        print(f"Car {car.id} picked up Passenger {passenger.id}")
                    elif car.status == Car.Status.DROPPING_OFF:
                        self.drop_passenger(passenger)
                        car.drop_passenger()
                        passenger.drop_off()
                        print(f"Car {car.id} dropped off Passenger {passenger.id}")

                    car.stop()
                    continue  # Skip the path planning and control

                path, _ = A_star(free_space_with_obs, start, goal)
                smoothed_x, smoothed_y = smooth_path(path)
                distance_to_goal = car.control_along_path(smoothed_x, smoothed_y)

            end_time = time.time()
            elapsed_time = end_time - start_time
            # Sleep for a while to simulate the car's movement
            left_time = max(0, self.update_interval - elapsed_time)
            time.sleep(left_time)
            self.set_car_info(car)

        print(f"Car {car.id} has finished its task.")

    def run(self):
        # self.plot_map_with_entities()

        # 1. Assign passengers to cars
        car_ids, passenger_ids = self.assign_passengers()
        for car_id, passenger_id in zip(car_ids, passenger_ids):
            car: Car = self.cars[car_id]
            print(f"Car {car_id} to Passenger {passenger_id}")
            car.assign_passenger(passenger_id) 

        # 2. Move cars to passengers
        # 3. Pick up passengers
        # 4. Drop off passengers
        threads = []
        for car in self.cars:
            if car is not None:
                thread = threading.Thread(target=self.move_car, args=(car,))
                threads.append(thread)
                thread.start()

        # 5. Keep updating the mobile car's position
        mobile_thread = threading.Thread(target=self.refresh_mobile_car)
        mobile_thread.start()

        if self.visualize:
            while True:
                self.plot_map_with_entities()

        for thread in threads:
            thread.join()
        
        self.mobile_car.status = Car.Status.FINISHED
        mobile_thread.join()
        print("Stop refreshing the mobile car's position.")
        
        print("All cars have finished their tasks.")


if __name__ == "__main__":
    engine = None
    server_ip = "localhost"
    server_port = 8888
    system = System(engine, server_ip, server_port)
    print(system.set_car_info(system.cars[0]))
    print(system.get_car_info(system.mobile_car.id))
    map = system.get_map()
    map_utils.plot_map(map)
