FROM python:3.5-alpine
ARG APP_URL
RUN pip install Flask
RUN pip install Flask-Cors
RUN curl -o app.tar.gz ${APP_URL}
RUN tar -zxvf app.tar.gz
EXPOSE 65000
WORKDIR  MicroserviceB
CMD python -m org.team.omni.data.ingestor.DataIngestorService --host 0.0.0.0 --port 65000