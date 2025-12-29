package com.example.usefy.service.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.repository.chat.ChatMessageRepository;
import com.example.usefy.repository.chat.ChatSessionRepository;
import com.example.usefy.service.ai.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private ChatServiceImpl chatService;

    private ChatSession chat;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        chat = ChatSession.builder()
                .id(1L)
                .title("Test chat")
                .user(user)
                .build();
    }

    @Test
    void addUserMessageAndAiReply_shouldSaveUserAndAiMessages() {
        // given
        when(chatSessionRepository.findById(1L))
                .thenReturn(Optional.of(chat));

        when(aiService.generateAnswer(any(), any(), any()))
                .thenReturn("AI response");

        // when
        chatService.addUserMessageAndAiReply(1L, "Hello");

        // then
        verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
        verify(aiService).generateAnswer(eq("Hello"), any(), any());
    }
}
