FROM openjdk:8-jdk-alpine

ADD lib/simsun.ttc /usr/lib/jvm/java-1.8-openjdk/jre/lib/fonts/fallback/simsun.ttc
ADD lib/simsun.ttc /usr/share/fonts/simsun.ttc

RUN apk add --no-cache tini
RUN apk add --no-cache procps
RUN apk add --no-cache curl


#ENTRYPOINT ["/sbin/tini","--","java","-Dspring.profiles.active=${ACTIVE}","-jar", "/data/deploy/web-dev.jar"]