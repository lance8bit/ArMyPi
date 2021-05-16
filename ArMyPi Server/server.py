from armypi import armypi
from flask import Flask, render_template, Response, request, jsonify
from camera import VideoCamera
import _thread

pi_camera = VideoCamera(flip=False)
ArMyPi = armypi(status=True)

app = Flask(__name__)

def detect_motion():
    while ArMyPi.status:
        if ArMyPi.pir.motion_detected:
            print("Motion detected: " + ArMyPi.get_date_time())
            ArMyPi.add_notification("Motion detected: " + ArMyPi.get_date_time())
            ArMyPi.led.on()
            ArMyPi.buzzer.beep(on_time=0.3, off_time=0.5, n=1, background=False)
            ArMyPi.pir.wait_for_no_motion()
            ArMyPi.led.off()
            print("Motion stopped")

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

@app.route('/alarm_status')
def alarm_status():
    return jsonify(ArMyPi.get_status())

@app.route('/alarm_activate')
def alarm_activate():
    ArMyPi.status = True
    _thread.start_new_thread(detect_motion, ())
    return jsonify(ArMyPi.get_status())

@app.route('/alarm_deactivate')
def alarm_deactivate():
    ArMyPi.status = False
    return jsonify(ArMyPi.get_status())

@app.route('/alarm_registers')
def alarm_registers():
    # return jsonify(ArMyPi.get_registers())
    return jsonify(ArMyPi.get_registers())

if __name__ == "__main__":
    try:
        _thread.start_new_thread(detect_motion, ())
        app.run(host='0.0.0.0', debug = False, threaded = True)
        # app.run(host='0.0.0.0', ssl_context=('/etc/letsencrypt/live/armypi.sgonzalezbit.me/fullchain.pem','/etc/letsencrypt/live/armypi.sgonzalezbit.me/privkey.pem'), debug = False, threaded = True)
    except KeyboardInterrupt:
        exit()