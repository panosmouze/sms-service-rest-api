# sms-service/Dockerfile

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/sms-service-1.0-SNAPSHOT-runner.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
