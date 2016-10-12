from ss import app
import unittest 
import json

class DataIngestorTest(unittest.TestCase): 

    @classmethod
    def setUpClass(cls):
        pass 

    @classmethod
    def tearDownClass(cls):
        pass 

    def setUp(self):
        # creates a test client
        self.app = app.test_client()
        # propagate the exceptions to the test client
        self.app.testing = True 

    def tearDown(self):
        pass 

    def test_home_status_code(self):
        # sends HTTP GET request to the application
        # on the specified path

        nexrad_station='KVWX'
        month='07'
        day='14'
        year='2009'
        hour='02'
        minute='02'
        seconds='05'

        result = self.app.get("/nexrad/generate/url/" + nexrad_station +"/"+month+"/"+day+"/"+year+"/"+hour+"/"+minute+"/"+seconds)
  
        # assert the status code of the response
        self.assertEqual(result.status_code, 200) 

    def test_home_data(self):
        # sends HTTP GET request to the application
        # on the specified path

        nexrad_station='KVWX'
        month='07'
        day='14'
        year='2009'
        hour='02'
        minute='02'
        seconds='05'

        result = self.app.get("/nexrad/generate/url/" + nexrad_station +"/"+month+"/"+day+"/"+year+"/"+hour+"/"+minute+"/"+seconds)
  
        decoded=result.data.decode('utf-8')
        # assert the response data
        self.assertEqual(decoded, "/" + nexrad_station +"/"+month+"/"+day+"/"+year+"/"+hour+"/"+minute+"/"+seconds)

if __name__ == '__main__':
    unittest.main()