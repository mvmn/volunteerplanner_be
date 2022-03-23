FROM amazoncorretto:11

USER root

RUN yum update -y && \
    yum install -y unzip

VOLUME /data01

RUN mkdir -p /data01/microservice/logs

COPY build/libs/manager-api*.jar /data01/microservice/service.jar

EXPOSE 8080
ENTRYPOINT java $JVM_MEM_OPTS $JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar /data01/microservice/service.jar