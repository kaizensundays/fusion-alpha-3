FROM bellsoft/liberica-openjdk-alpine:8u262-10

RUN apk update; \
    apk add vim; \
    apk add htop;

COPY service.jar /opt/
COPY log4j2-0.xml /opt/

WORKDIR /opt

EXPOSE 7701

ENTRYPOINT ["java", "-Dlogging.config=log4j2-0.xml", "-Dlog4j.shutdownHookEnabled=false", "-Dserver.port=7701", "-Dspring.profiles.active=dev", "-Djava.net.preferIPv4Stack=true", "-jar", "service.jar"]
