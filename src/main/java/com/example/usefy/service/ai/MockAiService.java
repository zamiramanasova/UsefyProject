package com.example.usefy.service.ai;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockAiService implements AiService {

    @Override
    public String generateAnswer(String userMessage, List<String> chatHistory) {

        String context = chatHistory.isEmpty()
                ? "no previous messages"
                : String.join(" | ", chatHistory);

        return "AI response to: '" + userMessage +
                "' (context: " + context + ")";
    }
}
