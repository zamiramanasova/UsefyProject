package com.example.usefy.repository.course;

import com.example.usefy.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // список всех курсов (на будущее можно добавить сортировку)
    List<Course> findAllByOrderByIdAsc();
}
