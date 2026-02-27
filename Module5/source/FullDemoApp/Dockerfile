# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jre-alpine

# Set work directory
WORKDIR /app

# Copy built application JAR from Maven target/
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port (change 8080 to your application's port if needed)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-DHOSTNAME=host.docker.internal", "-jar", "app.jar"]
