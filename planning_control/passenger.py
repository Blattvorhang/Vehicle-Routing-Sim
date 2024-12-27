from enum import Enum

class Passenger:
    class Status(Enum):
        WAITING = 0
        PICKED_UP = 1
        DROPPED_OFF = 2
    
    def __init__(self, id, start_x, start_y, dest_x, dest_y, car_id=None):
        self.id = id
        self.start_x = start_x
        self.start_y = start_y
        self.dest_x = dest_x
        self.dest_y = dest_y
        self.car_id = car_id
        self.status = Passenger.Status.WAITING
        
    def pick_up(self, car_id):
        self.car_id = car_id
        self.status = Passenger.Status.PICKED_UP
        
    def drop_off(self):
        self.car_id = None
        self.status = Passenger.Status.DROPPED_OFF