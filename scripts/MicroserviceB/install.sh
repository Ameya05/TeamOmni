
cd /home/ubuntu/
echo '===============Building data-ingestor-service docker===============' >> /var/log/sga-docker.log 2>&1
docker build --no-cache --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniDataIngestor.tar.gz -t omni/python:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running data-ingestor-service docker===============' >> /var/log/sga-docker.log 2>&1
docker run -it -e DOCKER_HOST=`curl http://169.254.169.254/latest/meta-data/public-hostname/` --name 'data-ingestor-service' -h omni.python -p 65000:65000 -d omni/python:v1 >> /var/log/sga-docker.log 2>&1