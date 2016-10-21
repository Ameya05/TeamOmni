echo 'Removing existing docker instances' >> /var/log/sga-docker.log 2>&1
docker ps -a | grep 'omniStormDetector' | awk '{print $1}' | xargs --no-run-if-empty docker rm -f omniStormDetector