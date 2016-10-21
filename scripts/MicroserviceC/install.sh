echo '===============Building docker===============' >> /var/log/sga-docker.log 2>&1
docker build --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormDetector.tar.gz -t omniStormDetector:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running docker===============' >> /var/log/sga-docker.log 2>&1
docker run --name omniStormDetector -h storm.detector -e APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormDetector.tar.gz -p 8080:8080 -d -it omniStormDetector:v1 >> /var/log/sga-docker.log 2>&1 &