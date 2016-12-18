sudo apt-get update
sudo apt-get -y install python-pip
sudo apt-get -y install ruby2.0
sudo apt-get -y install wget
cd /home/Ubuntu
wget https://aws-codedeploy-us-east-1.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
echo "Finished installing AWS Code Deploy!!"
