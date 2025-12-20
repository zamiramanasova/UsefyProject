package com.example.usefy.service.course;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;

import java.util.List;

public interface CourseService {

    /**
     * Получить список всех курсов
     */
    List<Course> getAllCourses();

    /**
     * Получить курс по id
     */
    Course getCourseById(Long id);

    List<Section> getSectionsByCourse(Long courseId);

    Section getSection(Long sectionId);

    void enrollUserToCourse(String username, Long courseId);

    boolean isUserEnrolled(String username, Course course);

    boolean isSectionCompleted(String username, Long sectionId);

    void completeSection(String username, Long sectionId);


}

