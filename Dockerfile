FROM gradle:8.10.2-jdk17 AS build
WORKDIR /app

COPY src/ src/
COPY build.gradle .
COPY settings.gradle .

RUN gradle clean --no-daemon -x test build

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/iot-application-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]