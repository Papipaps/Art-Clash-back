FROM openjdk:11

RUN mkdir -p /app/target

WORKDIR /app

COPY . .

VOLUME /app/target

CMD ["java", "-jar", "artclash-service-0.0.1-SNAPSHOT.jar"]
