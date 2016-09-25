echo 'Copying Microservice D war to jetty webapps location' >> /var/log/sga-omni-jetty.log 2>&1
cp /home/ubuntu/MicroserviceD/target/*.war /opt/jetty/webapps/
echo 'Starting jetty service' >> /var/log/sga-omni-jetty.log 2>&1
sudo service jetty start >> /var/log/sga-omni-jetty.log 2>&1 &
sleep 10