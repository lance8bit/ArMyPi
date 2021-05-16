from gpiozero import LED
from gpiozero import MotionSensor
from gpiozero import Buzzer
import _thread

from flask import Flask, render_template, Response, request, jsonify
from camera import VideoCamera
import time
import threading
import os

# SETTUP GPIOS INPUTS
out = LED(17)
pir = MotionSensor(4)
bz = Buzzer(27)
bz.off()
out.off()
is_activated = True

pi_camera = VideoCamera(flip=False) # flip pi camera if upside down.

# App Globals (do not edit) || Flask object called app
app = Flask(__name__)

def detect_motion():
    while is_activated:
        pir.wait_for_motion()

        print("Motion detected")
        out.on()
        bz.beep(on_time=0.3, off_time=0.5, n=10, background=False)

        pir.wait_for_no_motion()
        out.off()
        print("Motion Stopped")

def get_motion_status():
    if pir.motion_detected:
        return True
    else:
        return False

# FLASK Server Functions
@app.route('/')
def index():
    return render_template('index.html') #you can customze index.html here

def gen(camera):
    #get camera frame
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route('/video_feed')
def video_feed():
    return Response(gen(pi_camera),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/motion_status')
def motion_status():
    output = get_motion_status()
    return jsonify(output)

@app.route('/alarm_status')
def alarm_status():
    return jsonify(is_activated)

@app.route('/alarm_activate')
def alarm_activate():
    global is_activated
    is_activated = True
    return jsonify(is_activated)

@app.route('/alarm_dissarm')
def alarm_dissarm():
    global is_activated 
    is_activated = False
    return jsonify(is_activated)

if __name__ == "__main__":
    try:
        _thread.start_new_thread(detect_motion, ())
        app.run(host='0.0.0.0', debug = False, threaded = True)
        # app.run(host='0.0.0.0', ssl_context=('/etc/letsencrypt/live/armypi.sgonzalezbit.me/fullchain.pem','/etc/letsencrypt/live/armypi.sgonzalezbit.me/privkey.pem'), debug = False, threaded = True)
    except KeyboardInterrupt:
        exit()