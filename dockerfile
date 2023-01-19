FROM openjdk:11
COPY src /home/app/src
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]