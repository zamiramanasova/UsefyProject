package com.example.usefy.web;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
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
@RequestMapping("/chats")
public class WebChatController {

    private final ChatService chatService;
    private final UserService userService;

    // Список всех чатов
    @GetMapping
    public String chatList(
            @AuthenticationPrincipal UserDetails principal,
            Model model
    ) {
        List<ChatSessionDto> sessions = chatService.getUserChatSessions(principal.getUsername());
        model.addAttribute("sessions", sessions);
        return "chats";
    }

    @GetMapping("/{id}")
    public String viewChat(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal,
            Model model
    ) {
        ChatSessionDetailDto session = chatService.getChatSessionDetail(id, principal.getUsername());
        model.addAttribute("currentSession", session);
        model.addAttribute("messages", session.getMessages());
        model.addAttribute("chatId", id);

        List<ChatSessionDto> sessions = chatService.getUserChatSessions(principal.getUsername());
        model.addAttribute("sessions", sessions);

        return "chats";
    }

    // Создание нового чата
    @PostMapping
    public String createChat(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) Long sectionId,
            @RequestParam String title
    ) {
        User user = userService.findByUsername(principal.getUsername());
        ChatSession chat;

        if (sectionId != null) {
            chat = chatService.getOrCreateSectionChat(user, sectionId);
        } else {
            chat = chatService.createChat(user, title);
        }

        return "redirect:/chats/" + chat.getId();
    }

    @PostMapping("/{id}/message")
    public String sendMessage(
            @PathVariable Long id,
            @RequestParam("content") String content  // явно указываем имя параметра
    ) {
        System.out.println(">>> Получено сообщение для чата " + id + ": " + content); // для отладки
        chatService.addUserMessageAndAiReply(id, content);
        return "redirect:/chats/" + id;
    }

    // Удаление чата
    @PostMapping("/{id}/delete")
    public String deleteChat(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal
    ) {
        chatService.deleteChatSession(id, principal.getUsername());
        return "redirect:/chats";
    }
}