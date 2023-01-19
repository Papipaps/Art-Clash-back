FROM openjdk:11
WORKDIR /app
ADD artclash-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]