echo 'Copying MicroserviceC war to jetty webapps location'
cd '/home/ubuntu/MicroserviceC/target'
cp *.war /opt/jetty/webapps/
echo 'Starting jetty service'
sudo service jetty start
sleep 10