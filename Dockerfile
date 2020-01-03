FROM openjdk:8-jdk-alpine

VOLUME /tmp

ARG PROJECT_ARTIFACTID=exaltic-api

ARG PROJECT_VERSION=1.0-SNAPSHOT

COPY target/${PROJECT_ARTIFACTID}-${PROJECT_VERSION}.jar app.jar

COPY src/main/resources src/main/resources

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
