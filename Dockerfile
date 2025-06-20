FROM openjdk:23-ea-17-jdk
WORKDIR /app
COPY ./target/users-microservice-0.0.1-SNAPSHOT.jar .
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "users-microservice-0.0.1-SNAPSHOT.jar"]