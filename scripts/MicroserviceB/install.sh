echo 'starting installation process' >> /var/log/sga-omni-python.log
cd '/home/ec2-user/MicroserviceB'

echo 'Activating virtualenv' >> /var/log/sga-omni-python.log  2>&1
pip install virtualenv >> /var/log/sga-omni-python.log  2>&1
virtualenv venv >> /var/log/sga-omni-python.log  2>&1
. venv/bin/activate >> /var/log/sga-omni-python.log  2>&1

export FLASK_APP=dataIngestor.py
flask run --host=0.0.0.0 >> /var/log/sga-omni-python.log 2>&1 &
