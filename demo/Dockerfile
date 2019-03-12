FROM java:openjdk-8-jre-alpine

LABEL version="2.0.4.RELEASE"

MAINTAINER Sim Wang <8966188@qq.com>

ADD target/x7-demo-2.0.4.RELEASE.jar /data/deploy/demo/app.jar

#RUN bash -c "touch /data/deploy/demo/app.jar"

EXPOSE 6661

ENTRYPOINT ["java","-Dspring.profiles.active=test","-jar", "/data/deploy/demo/app.jar"]