FROM anapsix/alpine-java:8u121b13_server-jre

MAINTAINER Frank Mueller <hotstepper13@sop-clan.com>

ADD gira-bridge-${project.version}-jar-with-dependencies.jar /gira-bridge-${project.version}-jar-with-dependencies.jar

#ENV HOMESERVER_IP
#ENV HOMESERVER_PORT
#ENV TOKEN
#ENV DEBUG=false

EXPOSE 4711
EXPOSE 1900

ENTRYPOINT ["/opt/jdk/jre/bin/java","-jar","/gira-bridge-${project.version}-jar-with-dependencies.jar","--homeserver-ip", "${HOMESERVER_IP}", "--homeserver-port", "${HOMESERVER_PORT}", "--token", "${TOKEN}", "--debug", "${DEBUG}"]
