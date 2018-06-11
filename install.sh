echo 'Removing existing docker instances for omniStormDetector' >> /var/log/sga-docker.log 2>&1
sudo docker ps -a | grep 'omniStormDetector' | awk '{print $1}' | xargs --no-run-if-empty docker rm -f

echo '===============Building omniStormDetector docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker build --no-cache -t omni/stormdetect:v1 .
echo '===============Running omniStormDetector docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it -e DOCKER_HOST="localhost" --name omniStormDetector -h storm.detector -p 8081:8080 -d omni/stormdetect:v1 >> /var/log/sga-docker.log 2>&1
