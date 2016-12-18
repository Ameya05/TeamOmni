sudo touch /var/log/sga-docker.log
sudo chmod 777 /var/log/sga-docker.log

echo 'Removing existing docker instances for data-ingestor-service' >> /var/log/sga-docker.log 2>&1
docker ps -a | grep 'data-ingestor-service' | awk '{print $1}' | xargs --no-run-if-empty docker rm -f