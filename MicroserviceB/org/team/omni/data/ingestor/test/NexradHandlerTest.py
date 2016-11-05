import unittest
from datetime import datetime

from org.team.omni.data.ingestor.NexradHandler import NexradHandler

DAY = 6

MONTH = 6

YEAR = 2016

KABR = 'KABR'


class NexradHandlerTest(unittest.TestCase):

    def setUp(self):
        self.nexrad = NexradHandler("https://noaa-nexrad-level2.s3.amazonaws.com")

    def test_fetch_available_years(self):
        years = self.nexrad.fetch_available_years()
        self.assertFalse(not years, "Years list was empty")

    def test_fetch_available_months(self):
        months = self.nexrad.fetch_available_months(YEAR)
        self.assertFalse(not months, "Months list was empty")

    def test_fetch_available_days(self):
        days = self.nexrad.fetch_available_days(YEAR, MONTH)
        self.assertFalse(not days, "Days list was empty")
        print(days)

    def test_available_stations(self):
        stations = self.nexrad.fetch_available_stations(YEAR, MONTH, DAY)
        self.assertFalse(not stations, "Station list was empty")

    def test_fetch_nexrad_file_urls(self):
        file_urls = self.nexrad.fetch_nexrad_file_urls(YEAR, MONTH, DAY, KABR)
        self.assertFalse(not file_urls, "File url list was empty")

    def test_fetch_closest_nexrad_file_url(self):
        nexrad_url = self.nexrad.fetch_closest_nexrad_file_url(datetime(YEAR, MONTH, DAY, 15, 23, 35), KABR)
        self.assertIsNotNone(nexrad_url, 'Nexrad url object could not be found')
