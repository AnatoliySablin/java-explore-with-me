FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY lombok.config ./lombok.config
COPY stats-service ./stats-service
WORKDIR /app/stats-service
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/stats-service/stats-server/target/*.jar stats-server.jar
ENTRYPOINT ["java","-jar","stats-server.jar"]
