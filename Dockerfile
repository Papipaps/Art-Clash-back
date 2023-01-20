FROM openjdk:11

RUN mkdir -p /app/target

WORKDIR /app

COPY . .

VOLUME /app/target

CMD ["java", "-jar", "target/app.jar"]
