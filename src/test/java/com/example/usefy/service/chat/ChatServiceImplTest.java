package com.example.usefy.service.chat;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.chat.MessageRole;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    private AiService aiService;

    @InjectMocks
    private ChatServiceImpl chatService;

    private User testUser;
    private ChatSession testSession;
    private ChatMessage testMessage;

    @BeforeEach
    void setUp() {
        // Инициализация testUser
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .passwordHash("hashedPassword")
                .build();

        // Инициализация testSession
        testSession = ChatSession.builder()
                .id(1L)
                .title("Test Chat Session")
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .build();

        // Инициализация testMessage
        testMessage = ChatMessage.builder()
                .id(1L)
                .content("Test message content")
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
        assertThat(result.get(0).getTitle()).isEqualTo("Test Chat Session");
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
        assertThat(result.getTitle()).isEqualTo("Test Chat Session");
        assertThat(result.getMessages()).isNotEmpty();
        assertThat(result.getMessages()).hasSize(1);
        assertThat(result.getMessages().get(0).getContent()).isEqualTo("Test message content");
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
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

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
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        // when/then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chatService.deleteChatSession(1L, "testuser")
        );

        assertThat(exception.getMessage()).contains("Access denied");
    }

    @Test
    void addUserMessageAndAiReply_ShouldSaveMessages() {
        // given
        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(aiService.generateAnswer(anyString(), anyList(), anyString()))
                .thenReturn("AI response message");
        when(chatMessageRepository.save(any(ChatMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        chatService.addUserMessageAndAiReply(1L, "Hello, AI!");

        // then
        verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
        verify(aiService).generateAnswer(eq("Hello, AI!"), anyList(), anyString());
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
}