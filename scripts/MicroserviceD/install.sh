
cd /home/ubuntu/
echo '===============Building omniStormClustering docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker build --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniStormClustering.tar.gz -t omni/stormcluster:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running omniStormClustering docker===============' >> /var/log/sga-docker.log 2>&1
sudo docker run -it -e DOCKER_HOST=`curl http://169.254.169.254/latest/meta-data/public-hostname/` --name omniStormClustering -h storm.detector -p 8082:8080 -d omni/stormcluster:v1 >> /var/log/sga-docker.log 2>&1