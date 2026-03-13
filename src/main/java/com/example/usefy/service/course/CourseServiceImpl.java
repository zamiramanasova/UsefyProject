package com.example.usefy.service.course;

import com.example.usefy.model.User;
import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.model.course.UserCourseProgress;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.course.CourseRepository;
import com.example.usefy.repository.course.SectionRepository;
import com.example.usefy.repository.course.UserCourseProgressRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final UserCourseProgressRepository progressRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Курс с ID {} не найден", id);
                    return new EntityNotFoundException("Course not found with id=" + id);
                });
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
        log.info("Запись пользователя {} на курс {}", username, courseId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Course course = getCourseById(courseId);
        user.getEnrolledCourses().add(course);
        userRepository.save(user);

        log.info("Пользователь {} успешно записан на курс {}", username, courseId);
    }

    @Override
    public boolean isUserEnrolled(String username, Course course) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getEnrolledCourses().contains(course);
    }

    @Override
    public boolean isSectionCompleted(String username, Long sectionId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        return progressRepository.existsByUserAndSection(user, section);
    }

    @Override
    public void completeSection(String username, Long sectionId) {
        log.info("Завершение секции {} пользователем {}", sectionId, username);

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        if (progressRepository.existsByUserAndSection(user, section)) {
            log.debug("Секция {} уже была завершена пользователем {}", sectionId, username);
            return;
        }

        var progress = UserCourseProgress.builder()
                .user(user)
                .course(section.getCourse())
                .section(section)
                .completed(true)
                .completedAt(java.time.LocalDateTime.now())
                .build();

        progressRepository.save(progress);
        log.info("Секция {} завершена пользователем {}", sectionId, username);
    }

    @Override
    public Map<Long, Boolean> markCompletedSections(String username, List<Section> sections) {

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<Long, Boolean> map = new HashMap<>();

        for (Section s : sections) {
            boolean done = progressRepository.existsByUserAndSection(user, s);
            map.put(s.getId(), done);
        }

        return map;
    }


    @Override
    public int getCompletedSectionsCount(String username, Long courseId) {

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = getCourseById(courseId);

        return progressRepository.countByUserAndCourseAndCompletedTrue(user, course);
    }

}
