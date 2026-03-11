package com.example.usefy.service.chat;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.chat.MessageRole;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.chat.ChatMessageRepository;
import com.example.usefy.repository.chat.ChatSessionRepository;
import com.example.usefy.repository.course.SectionRepository;
import com.example.usefy.service.ai.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private AiService aiService;  // Это мок, Spring о нём не знает, но это нормально

    @InjectMocks
    private ChatServiceImpl chatService;

    private User testUser;
    private ChatSession testSession;
    private ChatMessage testMessage;
    private Section testSection;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = Course.builder()
                .id(1L)
                .title("Test Course")
                .description("Test Description")
                .build();

        testSection = Section.builder()
                .id(1L)
                .content("Test Section Content")
                .orderIndex(1)
                .course(testCourse)
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .passwordHash("hash")
                .build();

        testSession = ChatSession.builder()
                .id(1L)
                .title("Test Chat")
                .user(testUser)
                .section(testSection)
                .createdAt(LocalDateTime.now())
                .build();

        testMessage = ChatMessage.builder()
                .id(1L)
                .content("Test message")
                .role(MessageRole.USER)
                .chatSession(testSession)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getUserChatSessions_ShouldReturnListOfSessions() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatSessionRepository.findByUserOrderByCreatedAtDesc(testUser))
                .thenReturn(List.of(testSession));
        when(chatMessageRepository.findByChatSessionOrderByCreatedAt(testSession))
                .thenReturn(List.of(testMessage));

        // when
        List<ChatSessionDto> result = chatService.getUserChatSessions("testuser");

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Chat");
        assertThat(result.get(0).getMessageCount()).isEqualTo(1);

        verify(userRepository).findByUsername("testuser");
        verify(chatSessionRepository).findByUserOrderByCreatedAtDesc(testUser);
    }

    @Test
    void getUserChatSessions_ShouldThrowException_WhenUserNotFound() {
        // given
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chatService.getUserChatSessions("unknown")
        );

        assertThat(exception.getMessage()).contains("User not found");
    }

    @Test
    void getChatSessionDetail_ShouldReturnSessionDetail() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(chatMessageRepository.findByChatSessionOrderByCreatedAt(testSession))
                .thenReturn(List.of(testMessage));

        // when
        ChatSessionDetailDto result = chatService.getChatSessionDetail(1L, "testuser");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Chat");
        assertThat(result.getMessages()).isNotEmpty();
        assertThat(result.getMessages()).hasSize(1);
        assertThat(result.getMessages().get(0).getContent()).isEqualTo("Test message");
        assertThat(result.getMessages().get(0).getRole()).isEqualTo("USER");
    }

    @Test
    void getChatSessionDetail_ShouldThrowException_WhenSessionNotFound() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatSessionRepository.findById(999L)).thenReturn(Optional.empty());

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chatService.getChatSessionDetail(999L, "testuser")
        );

        assertThat(exception.getMessage()).contains("Chat session not found");
    }

    @Test
    void getChatSessionDetail_ShouldThrowException_WhenAccessDenied() {
        // given
        User anotherUser = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@test.com")
                .passwordHash("hash")
                .build();

        testSession.setUser(anotherUser);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chatService.getChatSessionDetail(1L, "testuser")
        );

        assertThat(exception.getMessage()).contains("Access denied");
    }

    @Test
    void deleteChatSession_ShouldDeleteSession() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatSessionRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testSession));

        // when
        chatService.deleteChatSession(1L, "testuser");

        // then
        verify(chatSessionRepository).delete(testSession);
    }

    @Test
    void deleteChatSession_ShouldThrowException_WhenAccessDenied() {
        // given
        User anotherUser = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@test.com")
                .passwordHash("hash")
                .build();

        testSession.setUser(anotherUser);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(chatSessionRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.empty());

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chatService.deleteChatSession(1L, "testuser")
        );

        assertThat(exception.getMessage()).contains("Chat session not found");
        verify(chatSessionRepository, never()).delete(any());
    }

    @Test
    void addUserMessageAndAiReply_ShouldSaveMessages() {
        // given
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(aiService.generateAnswer(anyString(), anyList(), anyString(), anyString(), anyString()))
                .thenReturn("AI response message");  // обновлено!
        when(chatMessageRepository.save(any(ChatMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        chatService.addUserMessageAndAiReply(1L, "Hello, AI!");

        // then
        verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
        verify(aiService).generateAnswer(eq("Hello, AI!"), anyList(), anyString(), anyString(), anyString());
    }

    @Test
    void addUserMessageAndAiReply_ShouldThrowException_WhenChatNotFound() {
        // given
        when(chatSessionRepository.findById(999L)).thenReturn(Optional.empty());

        // when/then
        assertThrows(IllegalArgumentException.class,
                () -> chatService.addUserMessageAndAiReply(999L, "Hello")
        );
    }

    @Test
    void createNewSectionChat_ShouldCreateNewChat() {
        // given
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(testSection));
        when(chatSessionRepository.findByUserAndSectionOrderByCreatedAtDesc(testUser, testSection))
                .thenReturn(List.of(testSession));
        when(chatSessionRepository.save(any(ChatSession.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        ChatSession result = chatService.createNewSectionChat(testUser, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Чат 2"); // был один чат, создаём второй
        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getSection()).isEqualTo(testSection);

        verify(chatSessionRepository).save(any(ChatSession.class));
    }

    @Test
    void getSectionChats_ShouldReturnList() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(testSection));
        when(chatSessionRepository.findByUserAndSectionOrderByCreatedAtDesc(testUser, testSection))
                .thenReturn(List.of(testSession));
        when(chatMessageRepository.findByChatSessionOrderByCreatedAt(testSession))
                .thenReturn(List.of(testMessage));

        // when
        List<ChatSessionDto> result = chatService.getSectionChats("testuser", 1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }
}