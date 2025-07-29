# Use OpenJDK 17 or 20, whichever you prefer
FROM openjdk:17-jdk-slim

# Copy your built jar to the container
COPY target/fraud-0.0.1-SNAPSHOT.jar /app.jar

# Expose port your Spring Boot app listens on (usually 8080)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
