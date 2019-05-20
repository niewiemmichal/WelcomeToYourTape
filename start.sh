#!/bin/bash
MYSQL_VERSION=8.0.16
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
MYSQL_JAR=mysql-connector-java-$MYSQL_VERSION.jar
MYSQL_MAVEN=http://search.maven.org/remotecontent?filepath=mysql/mysql-connector-java/$MYSQL_VERSION/$MYSQL_JAR

echo "[INFO] Starting WildFly server"
nohup $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 > /dev/null 2>&1 &

until `${JBOSS_CLI} -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    echo "[INFO] Waiting for the server to boot..."
    sleep 1
done

echo "[INFO] Downloading MySQL driver"
curl --silent --location --output /tmp/$MYSQL_JAR --url $MYSQL_MAVEN

echo "[INFO] Deploying MySQL driver"
${JBOSS_CLI} --connect --command="deploy /tmp/$MYSQL_JAR"

echo "[INFO] Creating datasource"
${JBOSS_CLI} --connect --command="data-source add \
    --name=${DB_NAME}DS \
    --jndi-name=java:/jdbc/datasources/${DB_NAME}DS \
    --user-name=$DB_USERNAME \
    --password=$DB_PASSWORD \
    --driver-name=$MYSQL_JAR \
    --connection-url=jdbc:mysql://${DB_URI}/${DB_NAME}?autoReconnect=true
    --check-valid-connection-sql=\"SELECT 1\" \
    --background-validation=true \
    --background-validation-millis=60000 \
    --flush-strategy=IdleConnections \
    --min-pool-size=10 --max-pool-size=50  --pool-prefill=false"

echo "[INFO] Shutting down WildFly"
${JBOSS_CLI} --connect --command=":shutdown"

echo "[INFO] Preparing for application deployment"
cp /tmp/app.war $JBOSS_HOME/standalone/deployments/app.war
rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/*
rm -f /tmp/*.jar

echo "[INFO] Restarting WildFly"
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0