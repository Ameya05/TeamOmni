touch /var/log/sga-docker.log 2>&1
chmod 777 /var/log/sga-docker.log

echo 'Removing existing docker instances' >> /var/log/sga-docker.log 2>&1
sudo docker ps -a | grep '/omniStormClustering' | awk '{print $1}' | xargs --no-run-if-empty docker rm -f /omniStormClustering