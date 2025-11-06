FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY lombok.config ./lombok.config
COPY stats-service/pom.xml ./stats-service/pom.xml
COPY stats-service/stats-dto ./stats-service/stats-dto
COPY stats-service/stats-server/pom.xml ./stats-service/stats-server/pom.xml
COPY stats-service/stats-server/src ./stats-service/stats-server/src
COPY stats-service/stats-client ./stats-service/stats-client
WORKDIR /app/stats-service
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/stats-service/stats-server/target/*.jar stats-server.jar
ENTRYPOINT ["java","-jar","stats-server.jar"]
