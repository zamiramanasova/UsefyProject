package com.example.usefy.web;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chats")
public class WebChatController {

    private final ChatService chatService;
    private final UserService userService;

    // 1️⃣ Список чатов пользователя
    @GetMapping
    public String chatList(
            @AuthenticationPrincipal UserDetails principal,
            Model model
    ) {
        User user = userService.findByUsername(principal.getUsername());
        model.addAttribute("chats", chatService.getUserChats(user));
        return "chats";
    }

    // 2️⃣ Создание нового чата
    @PostMapping
    public String createChat(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam String title
    ) {
        User user = userService.findByUsername(principal.getUsername());
        ChatSession chat = chatService.createChat(user, title);
        return "redirect:/chats/" + chat.getId();
    }

    // 3️⃣ Просмотр конкретного чата
    @GetMapping("/{id}")
    public String viewChat(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("chatId", id);
        model.addAttribute("messages", chatService.getChatMessages(id));
        return "chat";
    }

    // 4️⃣ Отправка сообщения
    @PostMapping("/{id}/message")
    public String sendMessage(
            @PathVariable Long id,
            @RequestParam String content
    ) {
        chatService.addUserMessage(id, content);
        return "redirect:/chats/" + id;
    }
}
