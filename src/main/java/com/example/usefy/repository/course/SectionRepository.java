package com.example.usefy.repository.course;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByCourseOrderByOrderIndexAsc(Course course);
}
