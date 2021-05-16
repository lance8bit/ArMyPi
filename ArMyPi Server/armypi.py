from datetime import datetime
from gpiozero import LED
from gpiozero import MotionSensor
from gpiozero import Buzzer

class armypi:
    def __init__(self, status):
        self.status = status
        self.array_registros = []
        self.led = LED(17)
        self.pir = MotionSensor(4)
        self.buzzer = Buzzer(27)

    def get_status(self):
        return self.status

    def get_date_time(self):
        return datetime.now().strftime("%d/%m/%Y %H:%M:%S")

    def set_status(self, new_status):
        self.status = new_status

    def add_notification(self, entrada):
        self.array_registros.append(entrada)

    def get_registers(self):
        return self.array_registros


# alarm1 = armypi(False)
# print(alarm1.get_date_time())

# alarm1.set_status(True)