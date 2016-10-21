echo '===============Building docker===============' >> /var/log/sga-docker.log 2>&1
docker build --build-arg APP_URL=https://s3-us-west-2.amazonaws.com/sga-team-omni/omniDataIngestor.tar.gz -t omni/python:v1 . >> /var/log/sga-docker.log 2>&1
echo '===============Running docker===============' >> /var/log/sga-docker.log 2>&1
docker run -it --name data-ingestor-service -h omni.python -p 65000:65000 -d omni/python:v1