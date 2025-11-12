FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY main-service/target/*.jar main-service.jar
ENTRYPOINT ["java","-jar","main-service.jar"]

