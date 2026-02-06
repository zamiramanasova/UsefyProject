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
        int completed = 0;

        if (principal != null) {
            enrolled = courseService.isUserEnrolled(principal.getUsername(), course);

            if (enrolled) {
                completed = courseService.getCompletedSectionsCount(
                        principal.getUsername(),
                        id
                );
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("sections", sections);
        model.addAttribute("enrolled", enrolled);
        model.addAttribute("completed", completed);
        model.addAttribute("total", sections.size());

        if (enrolled && principal != null) {
            model.addAttribute(
                    "completedSections",
                    courseService.markCompletedSections(principal.getUsername(), sections)
            );
        } else {
            model.addAttribute("completedSections", java.util.Map.of());
        }


        return "course";
    }


//    /**
//     * 3️⃣ Страница конкретного урока (секции)
//     * GET /courses/sections/{sectionId}
//     */
//    @GetMapping("/sections/{sectionId}")
//    public String sectionDetails(
//            @PathVariable Long sectionId,
//            Model model,
//            @AuthenticationPrincipal UserDetails user
//    ) {
//        Section section = courseService.getSection(sectionId);
//
//        boolean completed = courseService
//                .isSectionCompleted(user.getUsername(), sectionId);
//
//        model.addAttribute("section", section);
//        model.addAttribute("completed", completed);
//
//        return "section";
//    }
//


    @PostMapping("/{id}/enroll")
    public String enrollCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        courseService.enrollUserToCourse(principal.getUsername(), id);
        return "redirect:/profile";
    }

    @PostMapping("/sections/{sectionId}/complete")
    public String completeSection(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        courseService.completeSection(principal.getUsername(), sectionId);
        return "redirect:/courses/sections/" + sectionId;
    }

}
