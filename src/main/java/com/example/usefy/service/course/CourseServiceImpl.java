package com.example.usefy.service.course;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
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
}
