# Use a base image with JDK 22
FROM eclipse-temurin:22-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY target/LocationAlarmV2-Backend.jar /app/LocationAlarmV2-Backend.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "LocationAlarmV2-Backend.jar"]
