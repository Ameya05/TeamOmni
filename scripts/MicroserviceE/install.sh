
cd /home/ubuntu/
echo '===============Building forecast-trigger-service docker ===============' >> /var/log/sga-docker.log 2>&1
sudo docker build  --no-cache --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniForecastTrigger.tar.gz -t omni/forecast:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running forecast-trigger-service docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it -e DOCKER_HOST=`curl http://169.254.169.254/latest/meta-data/public-hostname/` --name 'forecast-trigger-service' -h storm.forecast -p 8083:8080 -d omni/forecast:v1 >> /var/log/sga-docker.log 2>&1