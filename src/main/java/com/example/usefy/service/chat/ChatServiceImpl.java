package com.example.usefy.service.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.chat.MessageRole;
import com.example.usefy.repository.chat.ChatMessageRepository;
import com.example.usefy.repository.chat.ChatSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatSession createChat(User user, String title) {
        ChatSession chat = ChatSession.builder()
                .user(user)
                .title(title)
                .build();

        return chatSessionRepository.save(chat);
    }

    @Override
    public List<ChatSession> getUserChats(User user) {
        return chatSessionRepository.findByUser(user);
    }

    @Override
    public ChatMessage addUserMessage(Long chatSessionId, String content) {
        ChatSession chat = chatSessionRepository.findById(chatSessionId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        ChatMessage message = ChatMessage.builder()
                .chatSession(chat)
                .content(content)
                .role(MessageRole.USER)
                .build();

        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getChatMessages(Long chatSessionId) {
        ChatSession chat = chatSessionRepository.findById(chatSessionId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        return chatMessageRepository.findByChatSessionOrderByCreatedAt(chat);
    }
}
