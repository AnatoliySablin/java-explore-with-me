FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY lombok.config ./lombok.config
COPY stats-service/pom.xml ./stats-service/pom.xml
COPY stats-service/stats-dto ./stats-service/stats-dto
COPY stats-service/stats-client ./stats-service/stats-client
COPY stats-service/stats-server/pom.xml ./stats-service/stats-server/pom.xml
COPY main-service/pom.xml ./main-service/pom.xml
COPY main-service/src ./main-service/src
RUN mvn clean package -DskipTests -pl main-service -am

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/main-service/target/*.jar main-service.jar
ENTRYPOINT ["java","-jar","main-service.jar"]

