echo 'Creating log file' >> /var/log/sga-omni-python.log 2>&1
sudo touch /var/log/sga-omni-python.log
echo 'killing running python instances' >> /var/log/sga-omni-python.log 2>&1
sudo pkill -9 -f python >> /var/log/sga-omni-python.log 2>&1 &