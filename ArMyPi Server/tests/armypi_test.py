import time
from gpiozero import MotionSensor
from flask import Flask, render_template, Response, request
from camera import VideoCamera
import time
import threading
import os


# PIR motion sensor configuration and detection

pir = MotionSensor(4)

while True:
    if pir.motion_detected:
        print('Motion detected')
        time.sleep(5)

# Video camera setup

pi_camera = VideoCamera(flip=False) # Flip pi camera if upside down.

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html') # Render html template

def gen(camera):
    # Get camera frame
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route('/video_feed')
def video_feed():
    return Response(gen(pi_camera),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=False)