package com.huangjien.aiBackend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestConfig {

    // This class can be used to define test-specific beans
    // For example, mock services or test data sources
    
    // Example: Mock external service dependencies
    // @Bean
    // @Primary
    // public SomeExternalService mockExternalService() {
    //     return Mockito.mock(SomeExternalService.class);
    // }
}