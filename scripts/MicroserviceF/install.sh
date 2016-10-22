echo '===============Building docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker build --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniRunForecast.tar.gz -t omni/runforecast:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it --name 'run-forecast-service' -h storm.forecast.run -p 8084:8080 -d omni/runforecast:v1