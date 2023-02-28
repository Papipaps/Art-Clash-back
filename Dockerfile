#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY src /app/src/
COPY pom.xml /app/
WORKDIR /app
RUN mvn clean package -Dskiptests

#
# Package stage
#
FROM openjdk:11
EXPOSE 8080
COPY --from=build /app/target/*.jar /usr/app/app.jar
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]