
wget -N https://s3.us-east-2.amazonaws.com/sga-weather/LoadBalanceAndServiceDiscovery-1.0.jar
mvn install:install-file -Dfile=LoadBalanceAndServiceDiscovery-1.0.jar -DgroupId=org.team.omni.weather -DartifactId=LoadBalanceAndServiceDiscovery -Dversion=1.0 -Dpackaging=jar

cd MicroserviceC && mvn clean package
cd ../
cp MicroserviceC/target/*.war .
tar -cvzf omniRunForecast.tar.gz MicroserviceC scripts *.war Dockerfile || true
mv omniRunForecast.tar.gz app.tar.gz


