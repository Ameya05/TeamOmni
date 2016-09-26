echo 'starting installation process' >> /var/log/sga-omni-python.log
cd '/home/ubuntu/'
mkdir python
chmod 777 python
cp -R /home/ubuntu/MicroserviceB/ /home/ubuntu/python

cd '/home/ubuntu/python/MicroserviceB'
export FLASK_APP=dataIngestor.py
flask run --host=0.0.0.0 --port=65000>> /var/log/sga-omni-python.log 2>&1 &
