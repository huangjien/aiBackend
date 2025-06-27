# AI Backend Application

A Spring Boot application with AI capabilities using Spring AI framework.

## Project Structure

```
src/
├── main/
│   ├── java/com/huangjien/aiBackend/
│   │   ├── Main.java                    # Main Spring Boot application class
│   │   └── controller/
│   │       └── RestApiController.java  # REST API endpoints
│   └── resources/
│       └── application.properties       # Application configuration
└── test/
    ├── java/com/huangjien/aiBackend/
    │   ├── MainTest.java                # Basic Spring context test
    │   ├── MainApplicationTest.java     # Main method and annotation tests
    │   ├── WebIntegrationTest.java      # Web layer integration tests
    │   ├── TestConfig.java              # Test-specific configuration class
    │   └── controller/
    │       ├── RestApiControllerTest.java           # Unit tests for REST API controller
    │       └── RestApiControllerIntegrationTest.java # Integration tests for REST API endpoints
    └── resources/
        └── application-test.properties  # Test configuration
```

## Prerequisites

### PostgreSQL Setup

Before running the application, ensure PostgreSQL is installed and running:

```bash
# Install PostgreSQL (macOS with Homebrew)
brew install postgresql
brew services start postgresql

# Create database
psql postgres
CREATE DATABASE aibackend;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE aibackend TO postgres;
\q
```

**Note**: Update the database credentials in `application.properties` if using different username/password.

## Building the Application

```bash
# Build the JAR file
./gradlew bootJar

# Build Docker image
./gradlew dockerBuild
```

## Running the Application

```bash
# Run locally
java -jar build/libs/aiBackend-0.2.jar

# Run with Docker
docker run -p 8080:8080 aibackend
```

## Unit Tests

The project includes comprehensive unit tests:

### Test Classes

1. **MainTest.java** - Tests Spring application context loading
2. **MainApplicationTest.java** - Tests main method execution and Spring Boot annotations
3. **WebIntegrationTest.java** - Integration tests for web layer
4. **TestConfig.java** - Test-specific bean configurations
5. **RestApiControllerTest.java** - Unit tests for REST API controller
6. **RestApiControllerIntegrationTest.java** - Integration tests for REST API endpoints with type-safe operations

### Running Tests

The project now supports full Gradle test execution with Java 24 compatibility:

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests RestApiControllerIntegrationTest

# Compile test classes
./gradlew compileTestJava

# Use the provided test runner script (alternative)
./run-tests.sh
```

**Note**: All Gradle compatibility issues have been resolved with the upgrade to Gradle 8.14. Tests can be executed using standard Gradle commands or individually using your IDE's test runner.

### Test Features

- **Context Loading**: Verifies Spring application context loads successfully
- **Main Method Testing**: Tests application startup with mocked SpringApplication
- **Annotation Verification**: Ensures proper Spring Boot annotations are present
- **Web Integration**: Tests web server startup and basic endpoint availability
- **Test Configuration**: Provides test-specific H2 database and logging configuration

### Test Configuration

Tests use a separate configuration profile with:
- H2 in-memory database for testing
- Debug logging for application components
- Disabled JMX and banner for cleaner test output

## Dependencies

- Spring Boot 3.5.0
- Spring AI 1.0.0
- PostgreSQL database support
- Spring Data JPA
- Spring Data REST
- Spring Boot Test (JUnit 5, Mockito, AssertJ)
- H2 database for testing

## Testing Strategy

The test suite follows Spring Boot testing best practices:

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test the application with embedded server and H2 in-memory database
- **Mocking**: Uses Mockito for external dependencies
- **Test Profiles**: Separate configuration for test environment
- **Test Database**: Uses H2 in-memory database for testing

## CI/CD with GitHub Actions

The project includes a GitHub Actions workflow that automatically:

1. **Builds** the Spring Boot application using Gradle
2. **Creates** Docker images with multiple tags
3. **Pushes** images to Docker Hub registry
4. **Scans** for security vulnerabilities using Trivy

### Workflow Triggers

- **Push to main/master**: Builds and pushes images
- **Tags (v*)**: Creates version-specific releases
- **Pull Requests**: Builds and tests (no push)

### Required Secrets

To use the GitHub Actions workflow, configure these repository secrets:

```
DOCKER_USERNAME=your-dockerhub-username
DOCKER_PASSWORD=your-dockerhub-password-or-token
```

### Docker Image Tags

The workflow automatically creates multiple tags:
- `latest` - Latest build from main branch
- `<version>` - Semantic version tags (e.g., `v1.0.0`)
- `<branch>` - Branch-specific tags

### Security Scanning

Trivy vulnerability scanner runs automatically and uploads results to GitHub Security tab.

## Troubleshooting

### Java Compatibility
The project is compatible with Java 21-24:
1. Gradle 8.14 provides Java 24 support
2. All compilation and test execution issues have been resolved
3. Type-safe REST template operations eliminate unchecked warnings
4. Tests use H2 in-memory database, so no external database setup required

### Docker Build
If Docker build fails:
1. Ensure the JAR file is built: `./gradlew bootJar`
2. Check that Docker is running
3. Verify the `.dockerignore` allows the JAR file

## Notes

- The application uses Java 21 (compatible with Java 24)
- Gradle 8.14 for build automation with Java 24 compatibility
- PostgreSQL is configured for data persistence
- H2 in-memory database is used for testing
- AI capabilities are provided through Spring AI with Ollama integration
- MCP (Model Context Protocol) client and server support included
- Test dependencies include JUnit 5, Mockito, and Spring Boot Test
- All tests use type-safe REST template operations with proper generic handling
- GitHub Actions workflow automates CI/CD pipeline with Docker image building and pushing
- Multi-platform Docker images (linux/amd64, linux/arm64) are built automatically
- Security vulnerability scanning is integrated into the CI/CD pipeline