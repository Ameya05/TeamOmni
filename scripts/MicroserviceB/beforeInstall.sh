echo 'Creating log file' >> /var/log/sga-omni-python.log 2>&1

sudo touch /var/log/sga-omni-python.log
echo 'killing running python instances' >> /var/log/sga-omni-python.log 2>&1
ps -ef | grep python | grep -v grep | awk '{print $2}' | xargs kill >> /var/log/sga-omni-python.log 2>&1 &