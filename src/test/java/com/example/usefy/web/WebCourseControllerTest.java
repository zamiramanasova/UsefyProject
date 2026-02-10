package com.example.usefy.web;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.service.course.CourseService;
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

@WebMvcTest(WebCourseController.class)
class WebCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void anonymous_shouldReturn401_whenOpenCourses() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticated_shouldSeeCourses() throws Exception {
        Course c = new Course();
        c.setId(1L);
        c.setTitle("Java");

        when(courseService.getAllCourses())
                .thenReturn(List.of(c));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courses"))
                .andExpect(view().name("courses"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void user_shouldOpenCoursePage() throws Exception {

        Course course = Course.builder()
                .id(1L)
                .title("Test Course")
                .description("Desc")
                .build();

        Section s1 = Section.builder()
                .id(1L)
                .content("Section 1")
                .orderIndex(1)
                .course(course)
                .build();

        when(courseService.getCourseById(1L)).thenReturn(course);
        when(courseService.getSectionsByCourse(1L))
                .thenReturn(List.of(s1));

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("course"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("sections"));
    }

}
