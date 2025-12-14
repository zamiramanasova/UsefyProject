package com.example.usefy.service.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;

import java.util.List;

public interface ChatService {

    ChatSession createChat(User user, String title);

    List<ChatSession> getUserChats(User user);

    ChatMessage addUserMessage(Long chatSessionId, String content);

    List<ChatMessage> getChatMessages(Long chatSessionId);

    void addUserMessageAndAiReply(Long chatId, String userMessage);
}
