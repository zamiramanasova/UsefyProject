package com.example.usefy.controller;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)   // отключаем фильтры Security в тестах
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_success() throws Exception {

        User mockUser = User.builder()
                .id(1L)
                .username("test")
                .email("test@mail.com")
                .passwordHash("hashed")
                .build();

        when(userService.registerUser("test", "1234", "test@mail.com"))
                .thenReturn(mockUser);

        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("test");
        request.setPassword("1234");
        request.setEmail("test@mail.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void login_success() throws Exception {

        User mockUser = User.builder()
                .username("test")
                .passwordHash("hashed")
                .build();

        when(userService.findByUsername("test")).thenReturn(mockUser);
        when(passwordEncoder.matches("1234", "hashed")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }
}
