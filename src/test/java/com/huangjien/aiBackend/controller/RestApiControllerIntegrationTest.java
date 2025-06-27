package com.huangjien.aiBackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.application.name=Integration Test Backend",
    "application.version=2.0.0-integration",
    "logging.level.com.huangjien.aiBackend=DEBUG"
})
class RestApiControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStatusEndpointSuccessfully() {
        String url = "http://localhost:" + port + "/api/status";
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        Map<String, Object> body = response.getBody();
        assertThat(body.get("status")).isEqualTo("UP");
        assertThat(body.get("application")).isEqualTo("Integration Test Backend");
        assertThat(body.get("version")).isEqualTo("2.0.0-integration");
        assertThat(body.get("timestamp")).isNotNull();
    }

    @Test
    void shouldReturnValidJsonResponse() throws Exception {
        String url = "http://localhost:" + port + "/api/status";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString())
                .contains("application/json");
        
        // Verify the response is valid JSON
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertThat(jsonNode.has("status")).isTrue();
        assertThat(jsonNode.has("timestamp")).isTrue();
        assertThat(jsonNode.has("application")).isTrue();
        assertThat(jsonNode.has("version")).isTrue();
    }

    @Test
    void shouldReturnRecentTimestamp() {
        String url = "http://localhost:" + port + "/api/status";
        
        LocalDateTime beforeRequest = LocalDateTime.now();
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        LocalDateTime afterRequest = LocalDateTime.now();
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Map<String, Object> body = response.getBody();
        String timestampStr = (String) body.get("timestamp");
        
        // Parse the timestamp and verify it's within the request timeframe
        LocalDateTime responseTimestamp = LocalDateTime.parse(timestampStr, 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        assertThat(responseTimestamp).isBetween(
                beforeRequest.minusSeconds(1), 
                afterRequest.plusSeconds(1)
        );
    }

    @Test
    void shouldHandleMultipleConcurrentRequests() {
        String url = "http://localhost:" + port + "/api/status";
        
        // Test multiple concurrent requests
        for (int i = 0; i < 5; i++) {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("status")).isEqualTo("UP");
        }
    }

    @Test
    void shouldReturnCorrectContentType() {
        String url = "http://localhost:" + port + "/api/status";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString())
                .contains("application/json");
    }
}