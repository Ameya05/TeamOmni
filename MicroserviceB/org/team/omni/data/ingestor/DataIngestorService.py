import argparse
import logging
import os
from configparser import ConfigParser
from datetime import datetime

from flask import Flask
from flask import make_response
from flask.json import jsonify
from kazoo.client import KazooClient

from org.team.omni.data.ingestor.NexradHandler import NexradHandler
from org.team.omni.data.ingestor.ServiceRegistration import ServiceRegistration


logging.basicConfig()
LOGGER = logging.getLogger('DataIngestorService')


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
    register_service(args.port)
    flask_app.run(host=args.host,port=args.port)



def fetch_zookeeper_address():
    config = ConfigParser()
    config.read("config.ini")
    if not config.sections():
        return "54.70.147.185:2181"
    else:
        return config["ZOOKEEPER"]["address"]

service_registration=None

def register_service(port):
    zk = KazooClient(hosts=fetch_zookeeper_address())
    zk.start()
    service_registration = ServiceRegistration(zk,os.getenv('DOCKER_HOST','localhost'),port)
    service_registration.register_service()







app = Flask(__name__)

nexrad_handler = NexradHandler("https://noaa-nexrad-level2.s3.amazonaws.com")




def create_response(obj):
    return jsonify({"data": obj})


@app.route(
    '/nexrad/generate/url/<nexrad_station>/<int:month>/<int:day>/<int:year>/<int:hour>/<int:minute>/<int:seconds>',
    methods=['GET'])
def genrate_nexrad_file_url(nexrad_station, month, day, year, hour, minute, seconds):
    return create_response(
        nexrad_handler.fetch_closest_nexrad_file_url(datetime(year, month, day, hour, minute, seconds),
                                                     nexrad_station).url)


@app.route('/nexrad/fetch/years', methods=['GET'])
def fetch_years():
    return create_response(nexrad_handler.fetch_available_years())


@app.route('/nexrad/fetch/months/<int:year>', methods=['GET'])
def fetch_months(year):
    return create_response(nexrad_handler.fetch_available_months(year))


@app.route('/nexrad/fetch/days/<int:year>/<int:month>', methods=['GET'])
def fetch_days(year, month):
    return create_response(nexrad_handler.fetch_available_days(year, month))


@app.route('/nexrad/fetch/stations/<int:year>/<int:month>/<int:day>', methods=['GET'])
def fetch_stations(year, month, day):
    return create_response(nexrad_handler.fetch_available_stations(year, month, day))


@app.errorhandler(500)
def internal_server_error(error):
    LOGGER.exception('Server Error: %s', error)
    resp = make_response("Server Error", 500)
    return resp

@app.before_request
def before_request():
    service_registration.update_work_load(1)

@app.after_request
def after_request(response):
    service_registration.update_work_load(-1)
    return response

@app.teardown_request
def tear_down_request(exception):
    service_registration.update_work_load(-1)



if __name__ == '__main__':
    execute_flask(app)
