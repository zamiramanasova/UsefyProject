package com.example.usefy.service.course;

import com.example.usefy.model.User;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found with id=" + id)
                );
    }

    @Override
    public List<Section> getSectionsByCourse(Long courseId) {
        Course course = getCourseById(courseId);
        return sectionRepository.findByCourseOrderByOrderIndexAsc(course);
    }

    @Override
    public Section getSection(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
    }

    @Override
    @Transactional
    public void enrollUserToCourse(String username, Long courseId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Course course = getCourseById(courseId);

        user.getEnrolledCourses().add(course);
        userRepository.save(user);
    }

}
