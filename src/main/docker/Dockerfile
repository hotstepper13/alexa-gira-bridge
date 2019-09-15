FROM anapsix/alpine-java:8_server-jre

MAINTAINER Frank Mueller <hotstepper13@sop-clan.com>

ADD gira-bridge-${project.version}-jar-with-dependencies.jar /gira-bridge-${project.version}-jar-with-dependencies.jar

ENV DEBUG=false
ENV CONFIG_FILE=/etc/gira-bridge/configuration.json
ENV HTTP_IP=""


EXPOSE 4711
EXPOSE 1900

ENTRYPOINT /opt/jdk/jre/bin/java -jar /gira-bridge-${project.version}-jar-with-dependencies.jar --config-file ${CONFIG_FILE} --debug ${DEBUG}
