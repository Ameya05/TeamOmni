import argparse
import logging
import os
from logging.handlers import RotatingFileHandler
from configparser import ConfigParser
from datetime import datetime

from flask import Flask
from flask import make_response
from flask.json import jsonify
from kazoo.client import KazooClient

from org.team.omni.data.ingestor.ServiceRegistration import ServiceRegistration

UNIT_WORK_LOAD = 1
UNIT_WORK_LOAD_DECREASE=UNIT_WORK_LOAD*-1

service_registration=None
app = Flask(__name__)


#Execute flask app taking into account the port
def execute_flask(flask_app, default_host="0.0.0.0", default_port=65000):
    parser = argparse.ArgumentParser()
    parser.add_argument("-H", "--host",
                        help="Hostname of the Flask app " + \
                             "[Default: %s]" % default_host,
                        default=default_host)
    parser.add_argument("-P", "--port",
                        help="Port for the Flask app " + \
                             "[Default: %s]" % default_port, type=int,
                        default=default_port)
    args = parser.parse_args()
    app.logger.info('args'+args)
    register_service(args.port)
    flask_app.run(host=args.host,port=args.port)



def fetch_zookeeper_address():
    config = ConfigParser()
    config.read("config.ini")
    if not config.sections():
        return "54.70.147.185:2181"
    else:
        return config["ZOOKEEPER"]["address"]


def register_service(port):
    zk = KazooClient(hosts=fetch_zookeeper_address())
    zk.start()
    service_registration = ServiceRegistration(zk,os.getenv('DOCKER_HOST','localhost'),port)
    service_registration.register_service()



def create_response(obj):
    return jsonify({"data": obj})


@app.route(
    '/nexrad/generate/url/<nexrad_station>/<int:month>/<int:day>/<int:year>/<int:hour>/<int:minute>/<int:seconds>',
    methods=['GET'])
def genrate_nexrad_file_url(nexrad_station, month, day, year, hour, minute, seconds):
    logging.getLogger().setLevel(logging.INFO)
    app.logger.info('Initiated Microservice B with input: ' + nexrad_station + month + day + year + hour + minute + seconds)
    s3key = ("/" + nexrad_station + "/" + month + "/" + day + "/" + year + "/" + hour + "/" + minute + "/" + seconds)

    app.logger.info('Returning to orchestration service with s3key:'+s3key)

    return s3key


@app.errorhandler(500)
def internal_server_error(error):
    LOGGER.exception('Server Error: %s', error)
    resp = make_response("Server Error", 500)
    return resp

@app.before_request
def before_request():
    service_registration.update_work_load(UNIT_WORK_LOAD)

@app.after_request
def after_request(response):
    service_registration.update_work_load(UNIT_WORK_LOAD_DECREASE)
    return response

@app.teardown_request
def tear_down_request(exception):
    service_registration.update_work_load(UNIT_WORK_LOAD_DECREASE)



if __name__ == '__main__':
    execute_flask(app)
