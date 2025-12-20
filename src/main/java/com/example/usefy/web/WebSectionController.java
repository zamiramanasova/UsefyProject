package com.example.usefy.web;

import com.example.usefy.model.course.Section;
import com.example.usefy.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sections")
public class WebSectionController {

    private final CourseService courseService;

    @GetMapping("/sections/{sectionId}")
    public String sectionDetails(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal,
            Model model
    ) {
        Section section = courseService.getSection(sectionId);

        if (principal == null) {
            return "redirect:/login";
        }

        boolean allowed = courseService.isUserEnrolled(
                principal.getUsername(),
                section.getCourse()
        );

        if (!allowed) {
            return "redirect:/courses/" + section.getCourse().getId();
        }

        model.addAttribute("section", section);
        return "section";
    }

}
