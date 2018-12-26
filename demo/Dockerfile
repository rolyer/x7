FROM centos7-java1.8

MAINTAINER Sim Wang <8966188@qq.com>

ADD x7-demo-1.0.2-SNAPSHOT.jar /data/deploy/demo/app.jar

RUN bash -c "touch /data/deploy/demo/app.jar"

EXPOSE 22 8080

ENTRYPOINT ["java","-Dspring.profiles.active=test","-jar", "/data/deploy/demo/app.jar"]