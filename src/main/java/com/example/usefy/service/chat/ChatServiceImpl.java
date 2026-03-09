package com.example.usefy.service.chat;

import com.example.usefy.dto.ChatMessageDto;
import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.chat.MessageRole;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.chat.ChatMessageRepository;
import com.example.usefy.repository.chat.ChatSessionRepository;
import com.example.usefy.repository.course.SectionRepository;
import com.example.usefy.service.ai.AiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final int CONTEXT_LIMIT = 5;

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final AiService aiService;

    // ============ СУЩЕСТВУЮЩИЕ МЕТОДЫ ============

    @Override
    @Transactional
    public void addUserMessageAndAiReply(Long chatId, String userMessage) {
        ChatSession chat = chatSessionRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        // Сохраняем сообщение пользователя
        ChatMessage userMsg = ChatMessage.builder()
                .chatSession(chat)
                .content(userMessage)
                .role(MessageRole.USER)
                .build();
        chatMessageRepository.save(userMsg);

        // Получаем контекст (последние 5 сообщений)
        List<String> context = chatMessageRepository
                .findTop5ByChatSessionOrderByCreatedAtDesc(chat)
                .stream()
                .map(ChatMessage::getContent)
                .toList();

        // Получаем текст урока
        String lessonText = "";
        if (chat.getSection() != null) {
            lessonText = chat.getSection().getContent();
        }

        // Получаем ответ от AI
        String aiAnswer = aiService.generateAnswer(userMessage, context, lessonText);

        // Сохраняем ответ AI
        ChatMessage aiMsg = ChatMessage.builder()
                .chatSession(chat)
                .content(aiAnswer)
                .role(MessageRole.AI)
                .build();
        chatMessageRepository.save(aiMsg);
    }

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
        return chatSessionRepository.findByUserOrderByCreatedAtDesc(user);
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

    @Override
    public ChatSession getOrCreateSectionChat(User user, Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));
        return getOrCreateSectionChat(user, section);
    }

    @Override
    public ChatSession getOrCreateSectionChat(User user, Section section) {
        // Получаем список всех чатов пользователя для этой секции
        List<ChatSession> existingChats = chatSessionRepository
                .findByUserAndSectionOrderByCreatedAtDesc(user, section);

        // Если чаты есть - возвращаем самый новый (первый в списке)
        if (!existingChats.isEmpty()) {
            return existingChats.get(0);
        }

        // Если нет - создаём новый
        ChatSession newChat = ChatSession.builder()
                .user(user)
                .section(section)
                .title("Чат к уроку " + section.getOrderIndex())
                .build();

        return chatSessionRepository.save(newChat);
    }

    @Override
    @Transactional
    public ChatSession createNewSectionChat(User user, Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + sectionId));

        // Подсчитываем, сколько уже чатов у пользователя в этой секции
        List<ChatSession> existingChats = chatSessionRepository
                .findByUserAndSectionOrderByCreatedAtDesc(user, section);

        String title = "Чат " + (existingChats.size() + 1);

        ChatSession newChat = ChatSession.builder()
                .user(user)
                .section(section)
                .title(title)
                .build();

        return chatSessionRepository.save(newChat);
    }

    @Override
    public List<ChatSessionDto> getSectionChats(String username, Long sectionId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + sectionId));

        List<ChatSession> chats = chatSessionRepository
                .findByUserAndSectionOrderByCreatedAtDesc(user, section);

        return chats.stream()
                .map(this::convertToDto)
                .toList();
    }

    // Вспомогательный метод для конвертации (можно добавить в конец класса)
    private ChatSessionDto convertToDto(ChatSession session) {
        List<ChatMessage> messages = chatMessageRepository
                .findByChatSessionOrderByCreatedAt(session);

        String lastMessage = messages.isEmpty() ? "" :
                messages.get(messages.size() - 1).getContent();
        String preview = lastMessage.length() > 30 ?
                lastMessage.substring(0, 30) + "..." : lastMessage;

        return new ChatSessionDto(
                session.getId(),
                session.getTitle(),
                session.getCreatedAt(),
                session.getSection() != null ? session.getSection().getId() : null,
                "Чат к уроку",
                messages.size(),
                preview
        );
    }

    // ============ НОВЫЕ МЕТОДЫ ============

    @Override
    public List<ChatSessionDto> getUserChatSessions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<ChatSession> sessions = chatSessionRepository.findByUserOrderByCreatedAtDesc(user);

        return sessions.stream()
                .map(session -> {
                    List<ChatMessage> messages = chatMessageRepository
                            .findByChatSessionOrderByCreatedAt(session);

                    String lastMessage = messages.isEmpty() ? "" :
                            messages.get(messages.size() - 1).getContent();
                    String preview = lastMessage.length() > 30 ?
                            lastMessage.substring(0, 30) + "..." : lastMessage;

                    return new ChatSessionDto(
                            session.getId(),
                            session.getTitle(),
                            session.getCreatedAt(),
                            session.getSection() != null ? session.getSection().getId() : null,
                            session.getSection() != null ?
                                    "Урок " + session.getSection().getOrderIndex() : "Общий чат",
                            messages.size(),
                            preview
                    );
                })
                .toList();
    }

    @Override
    public ChatSessionDetailDto getChatSessionDetail(Long sessionId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found: " + sessionId));

        // Проверяем, что чат принадлежит пользователю
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied to chat session: " + sessionId);
        }

        List<ChatMessage> messages = chatMessageRepository
                .findByChatSessionOrderByCreatedAt(session);

        List<ChatMessageDto> messageDtos = messages.stream()
                .map(msg -> new ChatMessageDto(
                        msg.getId(),
                        msg.getContent(),
                        msg.getRole().name(),
                        msg.getCreatedAt()
                ))
                .toList();

        return new ChatSessionDetailDto(
                session.getId(),
                session.getTitle(),
                session.getCreatedAt(),
                session.getSection() != null ? session.getSection().getId() : null,
                session.getSection() != null ?
                        "Урок " + session.getSection().getOrderIndex() : "Общий чат",
                messageDtos
        );
    }

    @Override
    @Transactional
    public void deleteChatSession(Long sessionId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Используем новый метод репозитория
        ChatSession session = chatSessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new RuntimeException("Chat session not found or access denied: " + sessionId));

        chatSessionRepository.delete(session);
    }
}