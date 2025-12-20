package com.example.usefy.repository.course;

import com.example.usefy.model.User;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.model.course.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {

    Optional<UserCourseProgress> findByUserAndSection(User user, Section section);

    List<UserCourseProgress> findByUserAndCourse(User user, Course course);

    boolean existsByUserAndSection(User user, Section section);
}
