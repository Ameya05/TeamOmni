
cd /home/ubuntu/
echo '===============Building omniStormDetector docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker build --no-cache --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormDetector.tar.gz -t omni/stormdetect:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running omniStormDetector docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it -e DOCKER_HOST=`curl http://169.254.169.254/latest/meta-data/public-hostname/` --name omniStormDetector -h storm.detector -p 8081:8080 -d omni/stormdetect:v1 >> /var/log/sga-docker.log 2>&1