FROM jfloff/alpine-python:latest
RUN apk --update  add curl ca-certificates libxml2-dev libxslt-dev 
COPY app.tar.gz app.tar.gz
RUN tar -zxvf app.tar.gz
EXPOSE 65000
WORKDIR  MicroserviceB
RUN pip install -r requirements.txt
CMD python -m org.team.omni.data.ingestor.DataIngestorService --host 0.0.0.0 --port 65000
