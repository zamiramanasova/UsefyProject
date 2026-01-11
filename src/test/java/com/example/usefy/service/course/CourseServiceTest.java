package com.example.usefy.service.course;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private CourseServiceImpl service;

    @Test
    void getAllCourses_shouldReturnList() {
        when(courseRepository.findAll())
                .thenReturn(List.of(new Course(), new Course()));

        List<Course> result = service.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseById_shouldReturnCourse() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        Course result = service.getCourseById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getCourseById_shouldThrow_whenNotFound() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getCourseById(1L));
    }

    @Test
    void getSectionsByCourse_shouldReturnSections() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(sectionRepository.findByCourseOrderByOrderIndexAsc(course))
                .thenReturn(List.of(new Section(), new Section()));

        List<Section> result = service.getSectionsByCourse(1L);

        assertEquals(2, result.size());
    }
}
