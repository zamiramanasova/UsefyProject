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


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebSectionController.class)
class WebSectionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    UserService userService;

    @MockBean
    ChatService chatService;

    @Test
    @WithMockUser(username = "test")
    void user_shouldOpenSection() throws Exception {

        Course course = Course.builder().id(1L).build();
        Section section = Section.builder()
                .id(10L)
                .course(course)
                .build();

        ChatSession chat = new ChatSession();
        chat.setId(1L);

        when(courseService.getSection(10L)).thenReturn(section);
        when(userService.findByUsername("test")).thenReturn(new User());
        when(chatService.getOrCreateSectionChat(any(User.class), eq(10L)))
                .thenReturn(chat);

        mockMvc.perform(get("/courses/sections/10"))
                .andExpect(status().isOk());
    }
}
