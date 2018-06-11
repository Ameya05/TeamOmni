FROM jetty
COPY app.tar.gz app.tar.gz
RUN tar -zxvf app.tar.gz
RUN cp *.war /var/lib/jetty/webapps
CMD ["java","-jar","/usr/local/jetty/start.jar"]
