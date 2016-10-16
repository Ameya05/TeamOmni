from flask import Flask

import logging
from logging.handlers import RotatingFileHandler


app = Flask(__name__)


# Current:
#   Generating Dummy URL
# TO DO:
#   Use S3 bucket to generate an actual URL for file


@app.route('/nexrad/generate/url/<nexrad_station>/<month>/<day>/<year>/<hour>/<minute>/<seconds>')
def hello_world(nexrad_station, month, day, year, hour, minute, seconds):
    app.logger.warning('Initiated Microservice B with input: ' + nexrad_station + month + day + year + hour + minute + seconds)
    # read a volume scan file on S3. I happen to know this file exists.
    # s3conn = boto.connect_s3()
    # bucket = s3conn.get_bucket('noaa-nexrad-level2')
    # for key in bucket.list():
    #    print(key.name.encode('utf-8'))
    # s3key= bucket.get_key('2015/05/15/KVWX/KVWX20150515_080737_V06.gz')
    s3key = ("/" + nexrad_station + "/" + month + "/" + day + "/" + year + "/" + hour + "/" + minute + "/" + seconds)

    app.logger.warning('Returning to orchestration service with s3key:'+s3key)

    return s3key


if __name__ == '__main__':
    app.debug = False
    formatter = logging.Formatter('[%(asctime)s] {%(pathname)s:%(lineno)d} - %(message)s')
    handler = RotatingFileHandler('MicroBlog.log', maxBytes=10000, backupCount=1)
    handler.setLevel(logging.INFO)
    handler.setFormatter(formatter)
    app.logger.addHandler(handler)
    app.run()
