FROM centos7-java1.8

MAINTAINER Sim Wang <8966188@qq.com>

ADD ./target/x7-demo-1.6.1-SNAPSHOT.jar /data/deploy/demo/app.jar

RUN bash -c "touch /data/deploy/demo/app.jar"

EXPOSE 22 8080 6661

ENTRYPOINT ["java","-Dspring.profiles.active=test","-jar", "/data/deploy/demo/app.jar"]