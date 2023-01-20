FROM openjdk:11

RUN mkdir -p /app/target

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/target/app.jar

VOLUME /app/target

CMD ["java", "-jar", "/app/target/app.jar"]
