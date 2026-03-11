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
    public String generateAnswer(String question, List<String> context, String lesson,
                                 String courseTitle, String lessonTitle) {
        log.info("🤖 Запрос к Gemini по курсу '{}', урок '{}'", courseTitle, lessonTitle);
        log.info("📚 Контекст диалога: {} предыдущих сообщений", context.size());

        try {
            // Формируем промпт с названием курса и урока
            String systemContext = String.format("""
                Ты — преподаватель по курсу "%s".
                Сейчас объясняешь тему: "%s"
                
                Материал текущего урока:
                %s
                """, courseTitle, lessonTitle, lesson);

            // История диалога
            String history = context.isEmpty() ? "" :
                    "История нашего разговора:\n" + String.join("\n---\n", context);

            String fullPrompt = String.format("""
                %s
                
                %s
                
                Вопрос студента: %s
                
                Ответь, учитывая всю историю разговора. Если вопрос связан с предыдущими ответами, продолжай логично.
                Будь дружелюбным, но профессиональным.
                """, systemContext, history, question);

            String answer = chatClient.prompt()
                    .user(fullPrompt)
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