FROM jboss/wildfly

COPY ./start.sh /tmp/
COPY build/libs/welcometoyourtape-1.0.war /tmp/app.war

CMD ["/tmp/start.sh"]