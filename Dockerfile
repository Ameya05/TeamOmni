FROM martinseeler/oracle-server-jre
ARG APP_URL

RUN apk add --upgrade tar

RUN apk --update add wget ca-certificates
	
RUN wget -O jetty.tar.gz "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.3.14.v20161028/jetty-distribution-9.3.14.v20161028.tar.gz" \
	&& mkdir 'jetty' \
	&& tar -xvf jetty.tar.gz -C jetty --strip-components=1 
	
RUN wget -O app.tar.gz ${APP_URL} \
	&& tar -zxf app.tar.gz -v -C jetty/webapps --wildcards '*.war'

WORKDIR  jetty
RUN echo '--module=logging' >> start.ini \
	&& echo '-Dorg.eclipse.jetty.util.log.class=org.eclipse.jetty.util.log.JavaUtilLog' >> start.ini \
	&& echo 'jetty.logging.dir=logs' >> start.ini \
	&& echo 'jetty.logging.append=true' >> start.ini \
	&& echo 'jetty.logging.timezone=UTC' >> start.ini

EXPOSE 8080
CMD ["java","-jar","start.jar"]