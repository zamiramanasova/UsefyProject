package com.example.usefy.web;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;
    private final UserService userService;

    // существующий метод
    @PostMapping("/{sectionId}")
    public void sendMessage(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam String message
    ) {
        var user = userService.findByUsername(principal.getUsername());
        var chat = chatService.getOrCreateSectionChat(user, sectionId);
        chatService.addUserMessageAndAiReply(chat.getId(), message);
    }

    // НОВЫЙ: список всех чатов пользователя
    @GetMapping("/sessions")
    public List<ChatSessionDto> getUserSessions(
            @AuthenticationPrincipal UserDetails principal
    ) {
        return chatService.getUserChatSessions(principal.getUsername());
    }

    // НОВЫЙ: детали конкретного чата
    @GetMapping("/sessions/{sessionId}")
    public ChatSessionDetailDto getSessionDetail(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return chatService.getChatSessionDetail(sessionId, principal.getUsername());
    }

    // НОВЫЙ: удаление чата (опционально)
    @DeleteMapping("/sessions/{sessionId}")
    public void deleteSession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        chatService.deleteChatSession(sessionId, principal.getUsername());
    }
}