package com.example.usefy.service.ai;

import org.springframework.stereotype.Service;

@Service
public class MockAiService implements AiService {

    @Override
    public String generateAnswer(String userMessage) {
        return "AI response to: " + userMessage;
    }
}
