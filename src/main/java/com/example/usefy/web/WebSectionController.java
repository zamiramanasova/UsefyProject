package com.example.usefy.web;

import com.example.usefy.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sections")
public class WebSectionController {

    private final CourseService courseService;

    @GetMapping("/{sectionId}")
    public String section(
            @PathVariable Long sectionId,
            Model model
    ) {
        model.addAttribute("section", courseService.getSection(sectionId));
        return "section";
    }
}
