package com.example.usefy.service.ai;

import java.util.List;

public interface AiService {

    /**
     * Генерирует ответ AI с учётом контекста урока и курса
     * @param question вопрос пользователя
     * @param context история диалога (список предыдущих сообщений)
     * @param lesson содержание урока
     * @param courseTitle название курса
     * @param lessonTitle название/номер урока
     * @return ответ AI
     */
    String generateAnswer(String question, List<String> context, String lesson,
                          String courseTitle, String lessonTitle);

}
