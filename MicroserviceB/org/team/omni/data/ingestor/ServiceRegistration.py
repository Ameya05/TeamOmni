import json
import time
import uuid
import logging


WORK_LOAD = "workLoad"
PAYLOAD = "payload"
SERVICE_NAME = "DataIngestionService"
BASE_SERVICE_NODE = "/services"
SERVICE_NODE = BASE_SERVICE_NODE + "/" + SERVICE_NAME

LOGGER = logging.getLogger('DataIngestorService')


class ServiceRegistration:

    def __init__(self, zk,address,port):
        self._zk = zk
        self._service_id = str(uuid.uuid4())
        self._service_instance_node = SERVICE_NODE + "/" + self._service_id
        self._service_instance = self.__create_default_service_instance(address,port)


    def register_service(self):
        LOGGER.info("Started Registering. ID: " + self._service_id)
        service_inst_json = json.dumps(self._service_instance)
        if not self._zk.exists(SERVICE_NODE):
            self._zk.create(SERVICE_NODE)
        if not self._zk.exists(self._service_instance_node):
            self._zk.create(self._service_instance_node, value=self.__get_service_instance_as_json_byte(), ephemeral=True)
        LOGGER.info("Registration Complete")

    def __get_service_instance_as_json_byte(self):
        return str.encode(json.dumps(self._service_instance))

    def update_work_load(self, work_load_change):
        work_load = self._service_instance[PAYLOAD][WORK_LOAD]
        self._service_instance[PAYLOAD][WORK_LOAD] = work_load + work_load_change
        self._zk.set(self._service_instance_node, self.__get_service_instance_as_json_byte())

    def __create_default_service_instance(self,address,port):
        return {
            "name": SERVICE_NAME,
            "id": self._service_id,
            "address": address,
            "port": port,
            "sslPort": None,
            "payload": {
                "@class": "org.team.omni.weather.InstanceDetails",
                "max_work_load": 100,
                "workLoad": 0
            },
            "registrationTimeUTC": int(round(time.time() * 1000)),
            "serviceType": "DYNAMIC",
            "uriSpec": {
                "parts": [{
                    "value": "scheme",
                    "variable": True
                }, {
                    "value": "://",
                    "variable": False
                }, {
                    "value": "address",
                    "variable": True
                }, {
                    "value": ":",
                    "variable": False
                }, {
                    "value": "port",
                    "variable": True
                }, {
                    "value": "/",
                    "variable": False
                }
                ]
            }
        }




