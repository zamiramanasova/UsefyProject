package com.example.usefy.service.course;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @PostConstruct
    public void init() {
        if (courseRepository.count() > 0) return;

        Course java = courseRepository.save(
                Course.builder()
                        .title("Java Basics")
                        .description("Основы Java")
                        .build()
        );

        sectionRepository.save(
                Section.builder()
                        .course(java)
                        .content("Variables and Data Types")
                        .orderIndex(1)
                        .build()
        );

        sectionRepository.save(
                Section.builder()
                        .course(java)
                        .content("Loops and Conditions")
                        .orderIndex(2)
                        .build()
        );
    }
}

