FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY lombok.config ./lombok.config
COPY checkstyle.xml ./checkstyle.xml
COPY suppressions.xml ./suppressions.xml
COPY stats-service ./stats-service
COPY main-service ./main-service
RUN mvn clean package -DskipTests -pl main-service -am

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/main-service/target/*.jar main-service.jar
ENTRYPOINT ["java","-jar","main-service.jar"]
