import json
from org.team.omni.data.ingestor.DataIngestorService import DataIngestorService
from nose.tools import assert_equal

app = DataIngestorService.app.test_client()

def test_status_code():
	nexrad_station='KVWX'
	month='07'
	day='14'
	year='2009'
	hour='02'
	minute='02'
	seconds='05'

	result = app.get("/nexrad/generate/url/" + nexrad_station +"/"+month+"/"+day+"/"+year+"/"+hour+"/"+minute+"/"+seconds)
	assert_equal(result.status_code, 200) 

def test_data_success():

	nexrad_station='KVWX'
	month='07'
	day='14'
	year='2009'
	hour='02'
	minute='02'
	seconds='05'

	result = app.get("/nexrad/generate/url/" + nexrad_station +"/"+month+"/"+day+"/"+year+"/"+hour+"/"+minute+"/"+seconds)
  
	decoded=result.data.decode('utf-8')
	# assert the response data
	assert_equal(decoded, "/" + nexrad_station +"/"+month+"/"+day+"/"+year+"/"+hour+"/"+minute+"/"+seconds)