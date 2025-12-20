package com.example.usefy.web;

import com.example.usefy.model.course.Course;
import com.example.usefy.model.course.Section;
import com.example.usefy.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class WebCourseController {

    private final CourseService courseService;

    /**
     * 1️⃣ Список всех курсов
     * GET /courses
     */
    @GetMapping
    public String courseList(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses";
    }

    /**
     * 2️⃣ Страница конкретного курса + его секции
     * GET /courses/{id}
     */
    @GetMapping("/{id}")
    public String courseDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal,
            Model model
    ) {
        Course course = courseService.getCourseById(id);
        List<Section> sections = courseService.getSectionsByCourse(id);

        boolean enrolled = false;

        if (principal != null) {
            enrolled = courseService.isUserEnrolled(principal.getUsername(), course);
        }

        model.addAttribute("course", course);
        model.addAttribute("sections", sections);
        model.addAttribute("enrolled", enrolled);

        return "course";
    }


    /**
     * 3️⃣ Страница конкретного урока (секции)
     * GET /courses/sections/{sectionId}
     */
    @GetMapping("/sections/{sectionId}")
    public String sectionDetails(
            @PathVariable Long sectionId,
            Model model
    ) {
        Section section = courseService.getSection(sectionId);
        model.addAttribute("section", section);
        return "section";
    }

    @PostMapping("/{id}/enroll")
    public String enrollCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        courseService.enrollUserToCourse(principal.getUsername(), id);
        return "redirect:/profile";
    }

}
