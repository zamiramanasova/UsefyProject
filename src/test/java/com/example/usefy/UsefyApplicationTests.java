package com.example.usefy;

import com.example.usefy.service.ai.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UsefyApplicationTests {

    @MockBean
    private AiService aiService;

    @Test
    void contextLoads() {
        // Просто проверяем, что контекст загружается
    }
}