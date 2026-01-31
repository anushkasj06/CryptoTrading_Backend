# Step 1: Build stage
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/treading-plateform-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Use Render's PORT environment variable
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
