from enum import Enum
import matlab.engine
import numpy as np


class Controller:
    def __init__(self, Kp=2.0, Ki=0.1, Kd=0.01, target_speed=0):
        self.Kp = Kp
        self.Ki = Ki
        self.Kd = Kd
        self.target_speed = target_speed
        self.integral = 0
        self.prev_error = 0
        
    def update_params(self, integral, prev_error):
        self.integral = integral
        self.prev_error = prev_error


class Car:
    class Status(Enum):
        IDLE = 0
        PICKING_UP = 1
        DROPPING_OFF = 2
        FINISHED = 3

    def __init__(self, engine, id, x, y, theta, dt=0.1):
        self.engine = engine
        self.id = id
        self.x: float = x
        self.y: float = y
        self.theta = theta
        self.dt = dt
        self.wheelbase = 2.9
        self.speed = 0
        self.max_speed = 10
        self.controller = Controller(target_speed=self.max_speed)

        self.passenger_id = None
        self.status = Car.Status.IDLE

    def assign_passenger(self, passenger_id):
        self.passenger_id = passenger_id
        self.status = Car.Status.PICKING_UP

    def pick_passenger(self):
        """Picked up the passenger."""
        self.status = Car.Status.DROPPING_OFF

    def drop_passenger(self):
        """Dropped off the passenger."""
        self.passenger_id = None
        self.status = Car.Status.FINISHED

    def control_along_path(self, cx, cy):
        """Control the car to move along the path."""
        # Convert the path to MATLAB-compatible format.
        # Note: cx and cy must be contiguous arrays instead of lists.
        # cx = np.ascontiguousarray(path[:, 0], dtype=np.float64)
        # cy = np.ascontiguousarray(path[:, 1], dtype=np.float64)
        trajectory = {
            'cx': matlab.double(cx),
            'cy': matlab.double(cy),
        }
        vehicle = {
            'x': matlab.double(self.x),
            'y': matlab.double(self.y),
            'theta': matlab.double(self.theta),
            'speed': matlab.double(self.speed),
            'L': matlab.double(self.wheelbase),
            'max_speed': matlab.double(self.max_speed),
        }
        pid_controller = {
            'Kp': matlab.double(self.controller.Kp),
            'Ki': matlab.double(self.controller.Ki),
            'Kd': matlab.double(self.controller.Kd),
            'target_speed': matlab.double(self.controller.target_speed),
            'integral': matlab.double(self.controller.integral),
            'prev_error': matlab.double(self.controller.prev_error),
        }
        car_state, distance_to_goal, pid_next = self.engine.car_control(
            trajectory,
            vehicle,
            pid_controller,
            matlab.double(self.dt),
            nargout=3
        )

        self.x = car_state["x"]
        self.y = car_state["y"]
        self.theta = car_state["theta"]
        self.speed = car_state["speed"]

        self.controller.update_params(pid_next["integral"], pid_next["prev_error"])

        return distance_to_goal
    
    def stop(self):
        self.speed = 0
        self.controller.update_params(0, 0)
    