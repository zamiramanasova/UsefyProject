package com.example.usefy.service.ai;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockAiService implements AiService {

    @Override
    public String generateAnswer(String question, List<String> context, String lesson) {

        return "AI ответ по уроку:\n\n"
                + lesson + "\n\n"
                + "Вопрос: " + question + "\n"
                + "Контекст: " + String.join(" | ", context);
    }

}
