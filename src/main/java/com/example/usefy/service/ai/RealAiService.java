package com.example.usefy.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Primary
public class RealAiService implements AiService {

    private final ChatClient chatClient;
    private final Resource systemPromptResource;

    public RealAiService(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:/prompts/system-prompt.st") Resource systemPromptResource
    ) {
        this.systemPromptResource = systemPromptResource;

        // Пока инициализируем без системного промпта (добавим в generateAnswer)
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String generateAnswer(String question, List<String> context, String lesson) {
        log.info("🤖 Запрос к Gemini: {}", question);
        log.info("📚 Контекст диалога: {} предыдущих сообщений", context.size());

        try {
            // Определяем название курса и урока (если есть)
            String courseTitle = extractCourseTitle(lesson);
            String lessonTitle = extractLessonTitle(lesson);

            // Загружаем промпт из файла и подставляем параметры
            PromptTemplate promptTemplate = new PromptTemplate(systemPromptResource);
            String systemMessage = promptTemplate.render(Map.of(
                    "courseTitle", courseTitle,
                    "lessonTitle", lessonTitle,
                    "lessonContent", lesson
            ));

            // Формируем историю диалога
            String history = context.isEmpty() ? "" :
                    "История диалога:\n" + String.join("\n---\n", context);

            String userPrompt = String.format("""
                %s
                
                Вопрос студента: %s
                
                Ответь, учитывая всю историю разговора и правила выше.
                """, history, question);

            String answer = chatClient.prompt()
                    .system(systemMessage)
                    .user(userPrompt)
                    .call()
                    .content();

            log.info("✅ Ответ получен, длина: {} символов", answer.length());
            return answer;

        } catch (Exception e) {
            log.error("❌ Ошибка при обращении к Gemini: {}", e.getMessage(), e);
            return "Извините, произошла ошибка при обращении к AI. Попробуйте позже.";
        }
    }

    /**
     * Извлекает название курса из текста урока (первые 50 символов)
     */
    private String extractCourseTitle(String lesson) {
        if (lesson == null || lesson.isEmpty()) {
            return "Программирование";
        }
        // Можно улучшить, если передавать из контроллера
        return "Java";
    }

    /**
     * Извлекает название урока из текста (первые 30 символов)
     */
    private String extractLessonTitle(String lesson) {
        if (lesson == null || lesson.isEmpty()) {
            return "текущий урок";
        }
        return lesson.length() > 30 ? lesson.substring(0, 30) + "..." : lesson;
    }
}