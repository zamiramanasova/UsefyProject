package com.example.usefy.web;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import com.example.usefy.service.course.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebSectionController.class)
class WebSectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserService userService;

    @MockBean
    private ChatService chatService;

    @Test
    @WithMockUser(username = "test")
    void user_shouldOpenSection() throws Exception {

        // ---------- GIVEN ----------
        Course course = Course.builder()
                .id(1L)
                .title("Test Course")
                .build();

        Section section = Section.builder()
                .id(10L)
                .orderIndex(1)
                .content("Hello Section")
                .course(course)
                .build();

        User user = User.builder()
                .id(100L)
                .username("test")
                .email("test@test.com")
                .passwordHash("password")
                .build();

        ChatSession chat = ChatSession.builder()
                .id(5L)
                .section(section)
                .user(user)
                .build();

        // ---------- MOCKS ----------
        when(courseService.getSection(10L)).thenReturn(section);

        when(courseService.isUserEnrolled(
                eq("test"),
                eq(course)
        )).thenReturn(true);

        when(userService.findByUsername("test")).thenReturn(user);

        when(chatService.getOrCreateSectionChat(
                any(User.class),
                eq(10L)
        )).thenReturn(chat);

        when(chatService.getChatMessages(5L))
                .thenReturn(List.of());

        when(courseService.isSectionCompleted("test", 10L))
                .thenReturn(false);

        // ---------- WHEN / THEN ----------
        mockMvc.perform(get("/courses/sections/10"))
                .andExpect(status().isOk());
    }
}
