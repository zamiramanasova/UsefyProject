package com.example.usefy.web;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.course.Section;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
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
    private final UserService userService;
    private final ChatService chatService;

    @GetMapping("/{sectionId}")
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

        var user = userService.findByUsername(principal.getUsername());
        var chat = chatService.getOrCreateSectionChat(user, sectionId);

        model.addAttribute("chatId", chat.getId());

        // 👇 ВОТ ЭТО ДОБАВЬ
        model.addAttribute("messages", chatService.getChatMessages(chat.getId()));

        model.addAttribute("section", section);
        return "section";
    }




    @PostMapping("/{sectionId}/ask")
    public String askAi(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam String question
    ) {
        var user = userService.findByUsername(principal.getUsername());
        var chat = chatService.getOrCreateSectionChat(user, sectionId);

        chatService.addUserMessageAndAiReply(chat.getId(), question);

        return "redirect:/sections/" + sectionId;
    }

}
