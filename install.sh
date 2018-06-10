sudo touch /var/log/sga-docker.log
sudo chmod 777 /var/log/sga-docker.log

echo 'Removing existing docker instances for data-ingestor-service' >> /var/log/sga-docker.log 2>&1
docker ps -a | grep 'data-ingestor-service' | awk '{print $1}' | xargs --no-run-if-empty docker rm -f

echo '===============Building data-ingestor-service docker===============' >> /var/log/sga-docker.log 2>&1
docker build -t omni/python:v1 .
echo '===============Running data-ingestor-service docker===============' >> /var/log/sga-docker.log 2>&1
docker run -it --name 'data-ingestor-service' -h omni.python -p 65000:65000 -d omni/python:v1 >> /var/log/sga-docker.log 2>&1
