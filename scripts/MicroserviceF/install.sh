
cd /home/ubuntu/
echo '===============Building run-forecast-service docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker build --no-cache --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniRunForecast.tar.gz -t omni/runforecast:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running run-forecast-service docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it -e DOCKER_HOST=`curl http://169.254.169.254/latest/meta-data/public-hostname/` --name 'run-forecast-service' -h storm.forecast.run -p 8084:8080 -d omni/runforecast:v1 >> /var/log/sga-docker.log 2>&1