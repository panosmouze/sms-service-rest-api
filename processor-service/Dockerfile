# processor-service/Dockerfile

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/processor-service-1.0-SNAPSHOT-runner.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
