package com.example.usefy.service.ai;

import java.util.List;

public interface AiService {

    String generateAnswer(String userMessage, List<String> chatHistory);
}
