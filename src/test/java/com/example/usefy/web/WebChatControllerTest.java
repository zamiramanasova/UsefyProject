package com.example.usefy.web;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebChatController.class)
class WebChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private UserService userService;

    @Test
    void chatList_shouldRedirectToLogin_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/chats"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test")
    void chatList_shouldReturnChatsPage_whenAuthenticated() throws Exception {
        User user = User.builder().username("test").build();

        when(userService.findByUsername("test")).thenReturn(user);
        when(chatService.getUserChats(user)).thenReturn(List.of());

        mockMvc.perform(get("/chats"))
                .andExpect(status().isOk())
                .andExpect(view().name("chats"));
    }
}
