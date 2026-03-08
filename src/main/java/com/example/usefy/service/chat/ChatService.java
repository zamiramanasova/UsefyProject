package com.example.usefy.service.chat;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.course.Section;
import java.util.List;

public interface ChatService {

    ChatSession createChat(User user, String title);

    List<ChatSession> getUserChats(User user);

    ChatMessage addUserMessage(Long chatSessionId, String content);

    List<ChatMessage> getChatMessages(Long chatSessionId);

    void addUserMessageAndAiReply(Long chatId, String userMessage);

    ChatSession getOrCreateSectionChat(User user, Long sectionId);

    ChatSession getOrCreateSectionChat(User user, Section section);

    List<ChatSessionDto> getUserChatSessions(String username);

    ChatSessionDetailDto getChatSessionDetail(Long sessionId, String username);

    void deleteChatSession(Long sessionId, String username); // опционально
}
