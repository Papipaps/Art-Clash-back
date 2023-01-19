FROM openjdk:11
COPY src /home/app/src
COPY target/artclash-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]