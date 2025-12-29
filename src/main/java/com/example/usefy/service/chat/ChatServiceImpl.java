package com.example.usefy.service.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.chat.MessageRole;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.chat.ChatMessageRepository;
import com.example.usefy.repository.chat.ChatSessionRepository;
import com.example.usefy.repository.course.SectionRepository;
import com.example.usefy.service.ai.AiService;
import com.example.usefy.service.course.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final int CONTEXT_LIMIT = 5;

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AiService aiService;
    private final SectionRepository sectionRepository;
    private final CourseService courseService;


    @Override
    public void addUserMessageAndAiReply(Long chatId, String userMessage) {

        ChatSession chat = chatSessionRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        // 1️⃣ сохраняем USER сообщение
        ChatMessage userMsg = ChatMessage.builder()
                .chatSession(chat)
                .content(userMessage)
                .role(MessageRole.USER)
                .build();

        chatMessageRepository.save(userMsg);

        // 2️⃣ получаем последние 5 сообщений для контекста
        List<String> context = chatMessageRepository
                .findTop5ByChatSessionOrderByCreatedAtDesc(chat)
                .stream()
                .map(ChatMessage::getContent)
                .toList();

        // 3️⃣ ДОБАВЛЯЕМ КОНТЕНТ УРОКА
        String lessonText = "";
        if (chat.getSection() != null) {
            lessonText = chat.getSection().getContent();
        }

        // 4️⃣ AI ответ
        String aiAnswer = aiService.generateAnswer(
                userMessage,
                context,
                lessonText
        );

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

        return chatSessionRepository.findByUserAndSection(user, section)
                .orElseGet(() -> {
                    ChatSession chat = ChatSession.builder()
                            .user(user)
                            .section(section)
                            .title("Чат по уроку " + section.getOrderIndex())
                            .build();

                    return chatSessionRepository.save(chat);
                });
    }


    @Override
    public ChatSession getOrCreateSectionChat(User user, Section section) {
        return chatSessionRepository.findByUserAndSection(user, section)
                .orElseGet(() -> chatSessionRepository.save(
                        ChatSession.builder()
                                .user(user)
                                .section(section)
                                .title("Chat for section " + section.getId())
                                .build()
                ));
    }

}
