echo 'Creating log file' >> /var/log/sga-apex-api-jetty.log 2>&1
sudo touch /var/log/sga-omni-jetty.log
echo 'killing jetty running instances' >> /var/log/sga-apex-api-jetty.log 2>&1
sudo pkill -9 -f jetty >> /var/log/sga-apex-api-jetty.log 2>&1
sleep 10