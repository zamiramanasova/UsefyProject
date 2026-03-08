package com.example.usefy.web;

import com.example.usefy.dto.ChatMessageDto;
import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatRestController.class)
class ChatRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean  // ВАЖНО: добавляем MockBean для UserService
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void getUserSessions_ShouldReturnSessions() throws Exception {
        // given
        List<ChatSessionDto> sessions = List.of(
                new ChatSessionDto(1L, "Chat 1", LocalDateTime.now(), 1L, "Section 1", 5, "last msg...")
        );
        when(chatService.getUserChatSessions("testuser")).thenReturn(sessions);

        // when/then
        mockMvc.perform(get("/api/chat/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Chat 1"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getSessionDetail_ShouldReturnSessionDetail() throws Exception {
        // given
        List<ChatMessageDto> messages = List.of(
                new ChatMessageDto(1L, "Hello", "USER", LocalDateTime.now())
        );
        ChatSessionDetailDto detail = new ChatSessionDetailDto(
                1L, "Chat 1", LocalDateTime.now(), 1L, "Section 1", messages
        );
        when(chatService.getChatSessionDetail(eq(1L), eq("testuser"))).thenReturn(detail);

        // when/then
        mockMvc.perform(get("/api/chat/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.messages[0].content").value("Hello"));
    }

    @Test
    void getUserSessions_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/chat/sessions"))
                .andExpect(status().isUnauthorized());
    }
}