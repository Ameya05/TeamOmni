import argparse
import logging
import os
from configparser import ConfigParser
from datetime import datetime
import time

from flask import Flask
from flask import make_response
from flask.json import jsonify
from kazoo.client import KazooClient

from org.team.omni.data.ingestor.NexradHandler import NexradHandler
from org.team.omni.data.ingestor.ServiceRegistration import ServiceRegistration

logging.basicConfig()

UNIT_WORK_LOAD = 1
UNIT_WORK_LOAD_DECREASE=UNIT_WORK_LOAD*-1

LOGGER = logging.getLogger('DataIngestorService')
SERVICE_REGISTRATION=None
APP = Flask(__name__)
NEXRAD_HANDLER = NexradHandler("https://noaa-nexrad-level2.s3.amazonaws.com")


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
    print(args)
    global SERVICE_REGISTRATION
    SERVICE_REGISTRATION=register_service(args.port)
    SERVICE_REGISTRATION.register_service()
    flask_app.run(host=args.host,port=args.port)


#fetch the zookeeper address from config.ini file
def fetch_zookeeper_address():
    config = ConfigParser()
    config.read("config.ini")
    if not config.sections():
        return "54.70.147.185:2181"
    else:
        return config["ZOOKEEPER"]["address"]

#create ServiceRegistartion instance and register the service
def register_service(port):
    zk = KazooClient(hosts=fetch_zookeeper_address())
    zk.start()
    return ServiceRegistration(zk,os.getenv('DOCKER_HOST','localhost'),port)



def create_response(obj):
    return jsonify({"data": obj})


@APP.route(
    '/nexrad/generate/url/<nexrad_station>/<int:month>/<int:day>/<int:year>/<int:hour>/<int:minute>/<int:seconds>',
    methods=['GET'])
def genrate_nexrad_file_url(nexrad_station, month, day, year, hour, minute, seconds):
    return create_response(
        NEXRAD_HANDLER.fetch_closest_nexrad_file_url(datetime(year, month, day, hour, minute, seconds),
                                                     nexrad_station).url)


@APP.route('/nexrad/fetch/years', methods=['GET'])
def fetch_years():
    return create_response(NEXRAD_HANDLER.fetch_available_years())


@APP.route('/nexrad/fetch/months/<int:year>', methods=['GET'])
def fetch_months(year):
    return create_response(NEXRAD_HANDLER.fetch_available_months(year))


@APP.route('/nexrad/fetch/days/<int:year>/<int:month>', methods=['GET'])
def fetch_days(year, month):
    return create_response(NEXRAD_HANDLER.fetch_available_days(year, month))


@APP.route('/nexrad/fetch/stations/<int:year>/<int:month>/<int:day>', methods=['GET'])
def fetch_stations(year, month, day):
    return create_response(NEXRAD_HANDLER.fetch_available_stations(year, month, day))


@APP.errorhandler(500)
def internal_server_error(error):
    LOGGER.exception('Server Error: %s', error)
    resp = make_response("Server Error", 500)
    return resp

@APP.before_request
def before_request():
    SERVICE_REGISTRATION.update_work_load(UNIT_WORK_LOAD)

@APP.after_request
def after_request(response):
    SERVICE_REGISTRATION.update_work_load(UNIT_WORK_LOAD_DECREASE)
    return response

@APP.teardown_request
def tear_down_request(exception):
    if exception:
    	SERVICE_REGISTRATION.update_work_load(UNIT_WORK_LOAD_DECREASE)




if __name__ == '__main__':
    execute_flask(APP)
