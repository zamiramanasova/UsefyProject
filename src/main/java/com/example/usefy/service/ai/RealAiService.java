package com.example.usefy.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
public class RealAiService implements AiService {

    private final ChatClient chatClient;

    public RealAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                    Ты — опытный преподаватель программирования. 
                    Отвечай на вопросы, основываясь на материале урока.
                    Будь дружелюбным, понятным и используй примеры кода, если нужно.
                    Отвечай на том же языке, на котором задан вопрос.
                    Важно: Помни весь предыдущий диалог и отвечай с учётом контекста.
                    """)
                .build();
    }

    @Override
    public String generateAnswer(String question, List<String> context, String lesson) {
        log.info("🤖 Запрос к Gemini: {}", question);
        log.info("📚 Контекст диалога: {} предыдущих сообщений", context.size());

        try {
            // Формируем полный контекст с учётом всей истории
            String fullContext = String.join("\n", context);

            String prompt = String.format("""
                Материал урока:
                %s
                
                История нашего диалога (более ранние сообщения):
                %s
                
                Текущий вопрос студента: %s
                
                Ответь, учитывая всю историю разговора. Если вопрос связан с предыдущими ответами, продолжай логично.
                """, lesson, fullContext, question);

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