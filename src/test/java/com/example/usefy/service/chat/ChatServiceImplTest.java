package com.example.usefy.service.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.chat.MessageRole;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.chat.ChatMessageRepository;
import com.example.usefy.repository.chat.ChatSessionRepository;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import com.example.usefy.service.ai.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ChatServiceImplTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    // AI мы МОКАЕМ — это правильно
    @MockBean
    private AiService aiService;

    private User user;

    private Section section;
    private Course course;

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CourseRepository courseRepository;


    @BeforeEach
    void setUp() {

        // USER
        user = new User();
        user.setUsername("test_user");
        user.setEmail("test@mail.com");
        user.setPasswordHash("password");
        user = userRepository.save(user);

        // COURSE  ← ВАЖНО
        course = new Course();
        course.setTitle("Test course");
        course.setDescription("Test description");
        course = courseRepository.save(course);

        // SECTION  ← ВАЖНО: section.course НЕ null
        section = new Section();
        section.setOrderIndex(1);
        section.setContent("Test lesson content");
        section.setCourse(course);   // ← ВОТ ЭТО РЕШАЕТ ОШИБКУ
        section = sectionRepository.save(section);

        // AI mock
        when(aiService.generateAnswer(
                anyString(),
                anyList(),
                anyString()
        )).thenReturn("AI test response");
    }



    // -----------------------------
    // getOrCreateSectionChat
    // -----------------------------

    @Test
    void shouldCreateChatIfNotExists() {
        ChatSession chat = chatService.getOrCreateSectionChat(user, 1L);

        assertThat(chat).isNotNull();
        assertThat(chat.getUser()).isEqualTo(user);
        assertEquals(section.getId(), chat.getSection().getId());


    }

    @Test
    void shouldReturnSameChatForSameUserAndSection() {
        ChatSession first = chatService.getOrCreateSectionChat(user, 1L);
        ChatSession second = chatService.getOrCreateSectionChat(user, 1L);

        assertThat(first.getId()).isEqualTo(second.getId());
    }

    // -----------------------------
    // addUserMessageAndAiReply
    // -----------------------------

    @Test
    void shouldSaveUserAndAiMessages() {
        ChatSession chat = chatService.getOrCreateSectionChat(user, 1L);

        chatService.addUserMessageAndAiReply(chat.getId(), "What is JVM?");

        List<ChatMessage> messages =
                chatMessageRepository.findByChatSessionOrderByCreatedAt(chat);

        assertThat(messages).hasSize(2);

        ChatMessage userMessage = messages.get(0);
        ChatMessage aiMessage = messages.get(1);

        assertThat(userMessage.getRole()).isEqualTo(MessageRole.USER);
        assertThat(userMessage.getContent()).isEqualTo("What is JVM?");

        assertThat(aiMessage.getRole()).isEqualTo(MessageRole.AI);
        assertThat(aiMessage.getContent()).isEqualTo("AI test response");
    }

    // -----------------------------
    // getChatMessages
    // -----------------------------

    @Test
    void shouldReturnMessagesInCorrectOrder() {
        ChatSession chat = chatService.getOrCreateSectionChat(user, 1L);

        chatService.addUserMessageAndAiReply(chat.getId(), "Q1");
        chatService.addUserMessageAndAiReply(chat.getId(), "Q2");

        List<ChatMessage> messages = chatService.getChatMessages(chat.getId());

        assertThat(messages).hasSize(4);
        assertThat(messages.get(0).getContent()).isEqualTo("Q1");
        assertThat(messages.get(2).getContent()).isEqualTo("Q2");
    }
}
