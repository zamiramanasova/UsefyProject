package com.example.usefy.web;

import com.example.usefy.model.course.Course;
import com.example.usefy.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class WebCourseController {

    private final CourseService courseService;

    /**
     * 📚 Список всех курсов
     * URL: GET /courses
     */
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses";
    }

    /**
     * 📖 Страница одного курса + его секции
     * URL: GET /courses/{id}
     */
    @GetMapping("/{id}")
    public String viewCourse(
            @PathVariable Long id,
            Model model
    ) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("sections", course.getSections());
        return "course";
    }

}

