package com.example.usefy.web;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.course.Section;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import com.example.usefy.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/courses/sections")
public class WebSectionController {

    private final CourseService courseService;
    private final UserService userService;
    private final ChatService chatService;

    // Страница секции со всеми чатами
    @GetMapping("/{sectionId}")
    public String sectionDetails(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long chatId,
            Model model
    ) {
        // Получаем секцию
        Section section = courseService.getSection(sectionId);

        // Проверяем, записан ли пользователь на курс
        boolean allowed = courseService.isUserEnrolled(
                principal.getUsername(),
                section.getCourse()
        );

        if (!allowed) {
            return "redirect:/courses/" + section.getCourse().getId();
        }

        // Получаем пользователя
        User user = userService.findByUsername(principal.getUsername());

        // Получаем ВСЕ чаты этой секции
        List<ChatSessionDto> sectionChats = chatService.getSectionChats(
                principal.getUsername(),
                sectionId
        );
        model.addAttribute("sectionChats", sectionChats);

        // Если chatId не указан и есть чаты - берём первый
        if (chatId == null && !sectionChats.isEmpty()) {
            chatId = sectionChats.get(0).getId();
        }

        // Загружаем сообщения выбранного чата
        if (chatId != null) {
            try {
                ChatSessionDetailDto chat = chatService.getChatSessionDetail(
                        chatId,
                        principal.getUsername()
                );
                model.addAttribute("currentChat", chat);
                model.addAttribute("messages", chat.getMessages());
                model.addAttribute("chatId", chatId);
            } catch (RuntimeException e) {
                // Если чат не найден или нет доступа
                return "redirect:/courses/sections/" + sectionId;
            }
        }

        model.addAttribute("section", section);
        return "section";
    }

    // Создание нового чата в секции
    @PostMapping("/{sectionId}/chats/new")
    public String createNewChat(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User user = userService.findByUsername(principal.getUsername());
        var chat = chatService.createNewSectionChat(user, sectionId);
        return "redirect:/courses/sections/" + sectionId + "?chatId=" + chat.getId();
    }

    // Удаление чата
    @PostMapping("/{sectionId}/chats/{chatId}/delete")
    public String deleteChat(
            @PathVariable Long sectionId,
            @PathVariable Long chatId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        chatService.deleteChatSession(chatId, principal.getUsername());
        return "redirect:/courses/sections/" + sectionId;
    }

    // Отправка сообщения (AJAX)
    @PostMapping("/{sectionId}/chats/{chatId}/messages")
    @ResponseBody
    public String sendMessage(
            @PathVariable Long sectionId,
            @PathVariable Long chatId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody String message
    ) {
        chatService.addUserMessageAndAiReply(chatId, message);
        return "OK";
    }
}