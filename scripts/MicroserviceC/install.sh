echo '===============Building docker===============' >> /var/log/sga-docker.log 2>&1
docker build --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormDetector.tar.gz -t omni/storm:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running docker===============' >> /var/log/sga-docker.log 2>&1
docker run --name storm -h storm.post -e APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormDetector.tar.gz -p 8080:8080 -d -it omni/storm:v1 >> /var/log/sga-docker.log 2>&1