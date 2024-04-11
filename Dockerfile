FROM openjdk:17-jdk-alpine
ARG JAR_FIlE=target/*.jar
COPY ./target/PeerToPeerApplication-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]