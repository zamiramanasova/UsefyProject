package com.example.usefy.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class MockAiService implements AiService {

    @Override
    public String generateAnswer(String question, List<String> context, String lesson,
                                 String courseTitle, String lessonTitle) {
        log.info("🤖 Mock AI получил вопрос: {}", question);
        log.info("📚 Курс: {}, Урок: {}", courseTitle, lessonTitle);

        return String.format("""
            [MOCK AI] Ответ по курсу "%s", урок "%s":
            
            Материал урока: %s
            
            На ваш вопрос: "%s"
            Я бы ответил примерно так...
            
            (Это тестовый ответ от Mock AI)
            """, courseTitle, lessonTitle, lesson, question);
    }
}