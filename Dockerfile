FROM openjdk:11-jdk-slim

ENV APP_HOME /app

RUN mkdir $APP_HOME

COPY target/*.jar $APP_HOME/app.jar

EXPOSE 8080

CMD ["java", "-jar", "$APP_HOME/app.jar"]
