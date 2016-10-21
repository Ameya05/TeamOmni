FROM python:2.7
ARG APP_URL
RUN pip install Flask
RUN pip install Flask-Cors
RUN curl -o app.tar.gz ${APP_URL}
RUN tar -zxvf app.tar.gz
EXPOSE 65000
CMD echo ls
WORKDIR  MicroserviceB
ENV FLASK_APP=dataIngestor.py
CMD python -m flask run --host=0.0.0.0 --port=65000