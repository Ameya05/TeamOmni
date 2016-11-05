from flask import Flask
from flask.json import jsonify
from flask import json
from flask import make_response
from datetime import datetime
import logging
from org.team.omni.data.ingestor.NexradHandler import NexradHandler

LOGGER = logging.getLogger('DataIngestorService')


app = Flask(__name__)

nexrad_handler=NexradHandler("https://noaa-nexrad-level2.s3.amazonaws.com")


def create_response(obj):
    return jsonify({"data":obj})


@app.route('/nexrad/generate/url/<nexrad_station>/<int:month>/<int:day>/<int:year>/<int:hour>/<int:minute>/<int:seconds>',methods=['GET'])
def genrate_nexrad_file_url(nexrad_station, month, day, year, hour, minute, seconds):
    return create_response(nexrad_handler.fetch_closest_nexrad_file_url(datetime(year,month,day,hour,minute,seconds),nexrad_station).url)

@app.route('/nexrad/fetch/years',methods=['GET'])
def fetch_years():
    return create_response(nexrad_handler.fetch_available_years())

@app.route('/nexrad/fetch/months/<int:year>',methods=['GET'])
def fetch_months(year):
    return create_response(nexrad_handler.fetch_available_months(year))

@app.route('/nexrad/fetch/days/<int:year>/<int:month>',methods=['GET'])
def fetch_days(year,month):
    return create_response(nexrad_handler.fetch_available_days(year,month))

@app.route('/nexrad/fetch/stations/<int:year>/<int:month>/<int:day>',methods=['GET'])
def fetch_stations(year,month,day):
    return create_response(nexrad_handler.fetch_available_stations(year,month,day))


@app.errorhandler(500)
def internal_server_error(error):
    LOGGER.exception('Server Error: %s', error)
    resp=make_response("Server Error",500)
    return resp


if __name__ == '__main__':
    app.run()
