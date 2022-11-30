FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar geocode-service.jar
ENTRYPOINT ["java", "-jar", "geocode-service.jar"]