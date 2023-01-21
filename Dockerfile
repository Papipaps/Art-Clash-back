#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY src /app/src/
COPY pom.xml /app/
WORKDIR /app
RUN mvn clean package

#
# Package stage
#
FROM openjdk:11
COPY --from=build /app/target/*.jar /usr/app/app.jar
ENTRYPOINT ["java","-jar", "-DskipTests","/usr/app/app.jar"]