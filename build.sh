pip install -r "MicroserviceB/requirements.txt"
cd "MicroserviceB" && python -m unittest discover -p "*Test.py"
cd ..
tar -cvzf app.tar.gz MicroserviceB scripts appspec.yml Dockerfile || true
