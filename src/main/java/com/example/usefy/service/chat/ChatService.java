package com.example.usefy.service.chat;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.course.Section;

import java.util.List;

public interface ChatService {

    // ============ СУЩЕСТВУЮЩИЕ МЕТОДЫ ============

    ChatSession createChat(User user, String title);

    List<ChatSession> getUserChats(User user);

    ChatMessage addUserMessage(Long chatSessionId, String content);

    List<ChatMessage> getChatMessages(Long chatSessionId);

    void addUserMessageAndAiReply(Long chatId, String userMessage);

    ChatSession getOrCreateSectionChat(User user, Long sectionId);

    ChatSession getOrCreateSectionChat(User user, Section section);

    // ============ НОВЫЕ МЕТОДЫ ============

    /**
     * Создаёт НОВЫЙ чат для секции (второй, третий и т.д.)
     */
    ChatSession createNewSectionChat(User user, Long sectionId);

    /**
     * Возвращает ВСЕ чаты пользователя для конкретной секции
     */
    List<ChatSessionDto> getSectionChats(String username, Long sectionId);

    /**
     * Удаляет чат по ID (с проверкой владельца)
     */
    void deleteChatSession(Long sessionId, String username);

    /**
     * Получает детали чата по ID (с проверкой владельца)
     */
    ChatSessionDetailDto getChatSessionDetail(Long sessionId, String username);

    /**
     * Возвращает все чаты пользователя (для списка)
     */
    List<ChatSessionDto> getUserChatSessions(String username);

    /**
    * Получить все сообщения чата для передачи в AI
     */
    List<ChatMessage> getFullChatHistory(Long chatId);
}