package com.example.usefy.service.course;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;

import java.util.List;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(Long courseId);

    List<Section> getSectionsByCourse(Long courseId);

    Section getSection(Long sectionId);
}

