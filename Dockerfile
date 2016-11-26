FROM jfloff/alpine-python:latest
ARG APP_URL
RUN apk --update -no-cache add curl ca-certificates libxml2-dev libxslt-dev 
RUN curl -o app.tar.gz https://s3-us-west-2.amazonaws.com/sga-team-omni/omniDataIngestor.tar.gz
RUN tar -zxvf app.tar.gz
EXPOSE 65000
WORKDIR  MicroserviceB
RUN pip install -r requirements.txt
CMD python -m org.team.omni.data.ingestor.DataIngestorService --host 0.0.0.0 --port 65000