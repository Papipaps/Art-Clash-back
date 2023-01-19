FROM openjdk:11-jdk-slim
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
