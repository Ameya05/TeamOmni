echo '===============Building docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker build --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormDetector.tar.gz -t omni/stormdetect:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it --name omniStormDetector -h storm.detector -p 8081:8080 -d omni/stormdetect:v1 >> /var/log/sga-docker.log 2>&1