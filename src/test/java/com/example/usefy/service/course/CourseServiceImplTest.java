package com.example.usefy.service.course;

import com.example.usefy.model.User;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void getAllCourses_shouldReturnList() {
        when(courseRepository.findAll())
                .thenReturn(List.of(new Course(), new Course()));

        List<Course> result = courseService.getAllCourses();

        assertThat(result).hasSize(2);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_shouldReturnCourse() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getCourseById_shouldThrow_whenNotFound() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getSectionsByCourse_shouldReturnSections() {
        Course course = new Course();
        course.setId(1L);

        Section s1 = new Section();
        s1.setOrderIndex(1);

        Section s2 = new Section();
        s2.setOrderIndex(2);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(sectionRepository.findByCourseOrderByOrderIndexAsc(course))
                .thenReturn(List.of(s1, s2));

        List<Section> result = courseService.getSectionsByCourse(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderIndex()).isEqualTo(1);
    }

    //зачисление
    @Test
    void enrollUserToCourse_shouldAddCourseToUser() {
        User user = new User();
        user.setUsername("test");
        user.setEnrolledCourses(new HashSet<>());

        Course course = new Course();
        course.setId(5L);

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        when(courseRepository.findById(5L))
                .thenReturn(Optional.of(course));

        courseService.enrollUserToCourse("test", 5L);

        assertThat(user.getEnrolledCourses())
                .contains(course);

        verify(userRepository).save(user);
    }

    @Test
    void enrollUserToCourse_shouldNotDuplicateEnrollment() {
        Course course = new Course();
        course.setId(3L);

        User user = new User();
        user.setUsername("test");
        user.setEnrolledCourses(new HashSet<>());

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        when(courseRepository.findById(3L))
                .thenReturn(Optional.of(course));

        courseService.enrollUserToCourse("test", 3L);

        assertThat(user.getEnrolledCourses()).hasSize(1);
        verify(userRepository).save(user);
    }
}
