# Stage 1: Build the application
FROM openjdk:21-slim AS build

WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create a lightweight final image
FROM openjdk:21-slim

WORKDIR /app
COPY --from=build /app/target/authservice-0.0.1-SNAPSHOT.jar /app/authservice.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/authservice.jar"]