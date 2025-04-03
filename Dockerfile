# Use Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy Maven files and dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the project
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight JDK image for running the application
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/SocialApp-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
