import logging
import re
from datetime import datetime
from datetime import timedelta

import requests
from lxml import etree

DEFAULT_DATA_TAG_NAME = 'Prefix'

DEFAULT_LIST_TAG_NAME = 'CommonPrefixes'

LOGGER = logging.getLogger('DataIngestorService')


class NexradFileDetails:

    def __init__(self, url, modified_date_time):
        self._url = url
        self._modified_date_time = modified_date_time

    @property
    def url(self):
        return self._url

    @url.setter
    def url(self, url):
        self._url = url

    @property
    def modified_date_time(self):
        return self._modified_date_time

    @modified_date_time.setter
    def modified_date_time(self, modified_date_time):
        self._modified_date_time = modified_date_time


class NexradHandler:
    def __init__(self, nexrad_url):
        self._url = nexrad_url
        self._details_fetch_base_url = nexrad_url + "/?delimiter=%2F&prefix="
        self._nexrad_url = nexrad_url

    def fetch_available_years(self):
        return list(
            map(lambda yr: int(yr.replace('/', '')),
                self.__fetch_details_from_nexrad(self._details_fetch_base_url, DEFAULT_LIST_TAG_NAME,
                                                 DEFAULT_DATA_TAG_NAME)))

    def fetch_available_months(self, year):
        return list(map(lambda month: int(re.search('[0-9]+/([0-9]+)/', month).group(1)),
                        self.__fetch_details_from_nexrad(self._details_fetch_base_url + str(year) + "%2F",
                                                         DEFAULT_LIST_TAG_NAME, DEFAULT_DATA_TAG_NAME)))

    def fetch_available_days(self, year, month):
        return list(
            map(lambda day: int(re.search('[0-9]+/[0-9]+/([0-9]+)/', day).group(1)), self.__fetch_details_from_nexrad(
                self._details_fetch_base_url + str(year) + "%2F" + self.__format_number(month) + "%2F",
                DEFAULT_LIST_TAG_NAME, DEFAULT_DATA_TAG_NAME)))

    def fetch_available_stations(self, year, month, day):
        return list(map(lambda station: re.search('[0-9]+/[0-9]+/[0-9]+/([a-zA-Z0-9]+)/', station).group(1),
                        self.__fetch_details_from_nexrad(
                            self._details_fetch_base_url + str(year) + "%2F" + self.__format_number(
                                month) + "%2F" + self.__format_number(day) + "%2F", DEFAULT_LIST_TAG_NAME,
                            DEFAULT_DATA_TAG_NAME)))

    def fetch_nexrad_file_urls(self, year, month, day, station):
        def post_processing(root, name_space):
            data = []
            contents_node_tag_name = name_space + 'Contents'
            file_path_tag_name = name_space + 'Key'
            modified_date_time_tag_name = name_space + 'LastModified'
            content_nodes = root.findall(contents_node_tag_name)
            for content_node in content_nodes:
                file_path = content_node.find(file_path_tag_name).text
                last_modified_date_time = self.__parse_date_time(content_node.find(modified_date_time_tag_name).text)
                data.append(NexradFileDetails(self._nexrad_url + '/' + file_path, last_modified_date_time))
            return data

        url = self._details_fetch_base_url + str(year) + "%2F" + self.__format_number(
            month) + "%2F" + self.__format_number(day) + "%2F" + station + "%2F"
        return self.__fetch_data_from_nexard(url, post_processing)

    def fetch_closest_nexrad_file_url(self, given_date_time, station):
        nexrad_urls = self.fetch_nexrad_file_urls(given_date_time.year, given_date_time.month, given_date_time.day,
                                                  station)
        min_time_delta = timedelta.max
        closest_matching_nexrad_url = None
        for nexrad_url in nexrad_urls:
            last_modiefied_date_time = nexrad_url.modified_date_time
            current_time_delta = abs(given_date_time - last_modiefied_date_time)
            if min_time_delta.total_seconds() > current_time_delta.total_seconds():
                min_time_delta = current_time_delta
                closest_matching_nexrad_url = nexrad_url
        return closest_matching_nexrad_url

    def __format_number(self, number):
        return '{0:02d}'.format(number)

    def __parse_date_time(self, date_time_str):
        return datetime.strptime(date_time_str, "%Y-%m-%dT%H:%M:%S.%fZ")

    def __fetch_details_from_nexrad(self, url, list_tag_name, data_tag_name):
        def post_processing(root, name_space):
            data = []
            common_prefix_node_tag_name = name_space + list_tag_name
            prefix_node_tag_name = name_space + data_tag_name
            data_nodes = root.findall(common_prefix_node_tag_name)
            for prefix_node in data_nodes:
                data.append(prefix_node.find(prefix_node_tag_name).text)
            LOGGER.info('Data parsing from xml completed')
            return data

        return self.__fetch_data_from_nexard(url, post_processing)

    def __fetch_data_from_nexard(self, url, post_processing):
        response = requests.get(url)
        if response.ok:
            LOGGER.info('Data Fetched from nexrad s3 bucket')
            root = etree.fromstring(response.content)
        else:
            raise Exception('Failed to fetch the list of data from nexrad')
        name_space = "{" + root.nsmap[None] + "}"
        return post_processing(root, name_space)
