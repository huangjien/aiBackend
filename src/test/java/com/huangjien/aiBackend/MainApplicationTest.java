package com.huangjien.aiBackend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(OutputCaptureExtension.class)
class MainApplicationTest {

    @Test
    void mainMethodShouldStartSpringApplication(CapturedOutput output) {
        try (MockedStatic<SpringApplication> springApplicationMock = Mockito.mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = Mockito.mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(eq(Main.class), any(String[].class)))
                    .thenReturn(mockContext);

            // Call the main method
            Main.main(new String[]{"--spring.main.web-environment=false"});

            // Verify SpringApplication.run was called with correct parameters
            springApplicationMock.verify(() -> SpringApplication.run(eq(Main.class), any(String[].class)));
            
            // Verify the welcome message is logged
            assertThat(output.getOut()).contains("Hello and welcome to AI Backend!");
        }
    }

    @Test
    void mainClassShouldHaveSpringBootApplicationAnnotation() {
        // Verify that the Main class has the @SpringBootApplication annotation
        assertThat(Main.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class))
                .isTrue();
    }
}