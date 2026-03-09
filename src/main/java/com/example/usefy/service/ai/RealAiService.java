package com.example.usefy.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@Primary
public class RealAiService implements AiService {

    private final ChatClient chatClient;

    public RealAiService(ChatClient.Builder chatClientBuilder) {
        // Самый простой способ - вообще без опций
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                    Ты — опытный преподаватель программирования. 
                    Отвечай на вопросы, основываясь на материале урока.
                    Будь дружелюбным, понятным и используй примеры кода, если нужно.
                    Отвечай на том же языке, на котором задан вопрос.
                    """)
                .build();
    }

    @Override
    public String generateAnswer(String question, List<String> context, String lesson) {
        log.info("🤖 Запрос к Gemini: {}", question);

        try {
            String prompt = String.format("""
                Материал урока:
                %s
                
                Вопрос студента: %s
                
                Дай подробный, понятный ответ с примерами, если это уместно.
                """, lesson, question);

            String answer = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("✅ Ответ получен, длина: {} символов", answer.length());
            return answer;

        } catch (Exception e) {
            log.error("❌ Ошибка при обращении к Gemini: {}", e.getMessage(), e);
            return "Извините, произошла ошибка при обращении к AI. Попробуйте позже.";
        }
    }
}