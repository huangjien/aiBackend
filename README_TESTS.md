# RestApiController Unit Tests

This document describes the unit tests created for the `RestApiController` class.

## Test Files Created

### 1. RestApiControllerTest.java
Location: `src/test/java/com/huangjien/aiBackend/controller/RestApiControllerTest.java`

**Test Type**: Unit Tests using `@WebMvcTest`

**Test Cases**:
- `shouldReturnStatusWithCorrectStructure()` - Verifies the JSON response structure and values
- `shouldReturnStatusWithDefaultValues()` - Tests fallback values when properties are not set
- `shouldReturnValidTimestamp()` - Validates timestamp format in ISO format
- `shouldReturnConsistentResponse()` - Tests multiple calls return consistent structure
- `shouldHaveCorrectResponseHeaders()` - Verifies correct Content-Type headers

**Features**:
- Uses MockMvc for fast, isolated testing
- Tests with custom property values via `@TestPropertySource`
- Validates JSON structure using JsonPath
- Includes timestamp format validation

### 2. RestApiControllerIntegrationTest.java
Location: `src/test/java/com/huangjien/aiBackend/controller/RestApiControllerIntegrationTest.java`

**Test Type**: Integration Tests using `@SpringBootTest`

**Test Cases**:
- `shouldReturnStatusEndpointSuccessfully()` - Full integration test with TestRestTemplate
- `shouldReturnValidJsonResponse()` - Validates complete JSON response parsing
- `shouldReturnRecentTimestamp()` - Tests timestamp accuracy within request timeframe
- `shouldHandleMultipleConcurrentRequests()` - Tests concurrent request handling
- `shouldReturnCorrectContentType()` - Verifies HTTP headers in integration context

**Features**:
- Uses TestRestTemplate for full HTTP testing
- Tests with H2 in-memory database
- Includes concurrent request testing
- Validates real HTTP response behavior

## Test Configuration

### Properties Used in Tests
```properties
# Unit Tests
spring.application.name=Test AI Backend
application.version=1.0.0-test

# Integration Tests
spring.application.name=Integration Test Backend
application.version=2.0.0-integration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

## Running Tests

### Manual Verification
Since there are Gradle configuration issues with the test task, you can verify the controller works by:

1. Start the application:
   ```bash
   ./gradlew bootRun
   ```

2. Test the endpoint:
   ```bash
   curl -X GET http://localhost:8080/api/status
   ```

   Expected response:
   ```json
   {
     "status": "UP",
     "timestamp": "2024-01-01T12:00:00.000",
     "application": "AI Backend",
     "version": "0.2.1"
   }
   ```

### Alternative Test Execution
If Gradle test issues are resolved, run:
```bash
./gradlew test --tests "*RestApiController*"
```

## Test Coverage

The tests cover:
- ✅ HTTP status codes (200 OK)
- ✅ JSON response structure
- ✅ Property injection (`@Value` annotations)
- ✅ Timestamp generation
- ✅ Content-Type headers
- ✅ Concurrent request handling
- ✅ Integration with Spring Boot context
- ✅ Database configuration (H2 for tests)

## Dependencies Required

The tests use these Spring Boot test dependencies (already in build.gradle):
- `spring-boot-starter-test` - Core testing framework
- `junit-platform-launcher` - JUnit 5 platform
- `h2database` - In-memory database for integration tests

## Notes

- Tests are designed to be independent and can run in any order
- Integration tests use random ports to avoid conflicts
- Both test classes include proper Spring Boot test annotations
- Tests validate both the happy path and edge cases
- Timestamp validation ensures the endpoint returns current time