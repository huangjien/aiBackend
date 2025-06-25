# Use Eclipse Temurin 21 Alpine runtime as base image (smaller than regular JRE)
FROM eclipse-temurin:21-jre-alpine

# Build argument for JAR version
ARG JAR_VERSION=0.1

# Install curl for health check (Alpine uses apk)
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file using dynamic version
COPY build/libs/aiBackend-${JAR_VERSION}.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080

# Run the application with optimized JVM settings for containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1