FROM jetty
ARG APP_URL
RUN curl -o app.tar.gz ${APP_URL}
RUN tar -zxvf app.tar.gz
RUN cp *.war /var/lib/jetty/webapps
CMD ["java","-jar","/usr/local/jetty/start.jar"]