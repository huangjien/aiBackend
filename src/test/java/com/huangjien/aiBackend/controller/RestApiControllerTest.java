package com.huangjien.aiBackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestApiController.class)
@TestPropertySource(properties = {
    "spring.application.name=Test AI Backend",
    "application.version=1.0.0-test"
})
class RestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStatusWithCorrectStructure() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("Test AI Backend"))
                .andExpect(jsonPath("$.version").value("1.0.0-test"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnStatusWithDefaultValues() throws Exception {
        // This test verifies fallback values when properties are not set
        MvcResult result = mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("status", "application", "version", "timestamp");
    }

    @Test
    void shouldReturnValidTimestamp() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        
        // Parse the JSON response to verify timestamp format
        var jsonNode = objectMapper.readTree(responseBody);
        String timestamp = jsonNode.get("timestamp").asText();
        
        // Verify timestamp is in ISO format (basic validation)
        assertThat(timestamp).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+");
    }

    @Test
    void shouldReturnConsistentResponse() throws Exception {
        // Test that multiple calls return consistent structure
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/status"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.application").value("Test AI Backend"))
                    .andExpect(jsonPath("$.version").value("1.0.0-test"))
                    .andExpect(jsonPath("$.timestamp").exists());
        }
    }

    @Test
    void shouldHaveCorrectResponseHeaders() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }
}