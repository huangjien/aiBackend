# Use Eclipse Temurin 21 runtime as base image
FROM eclipse-temurin:21-jre

# Build argument for JAR version
ARG JAR_VERSION=0.1

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file using dynamic version
COPY build/libs/aiBackend-${JAR_VERSION}.jar app.jar

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1