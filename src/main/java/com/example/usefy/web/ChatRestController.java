package com.example.usefy.web;

import com.example.usefy.dto.ChatSessionDetailDto;
import com.example.usefy.dto.ChatSessionDto;
import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    // ============ ЭНДПОИНТЫ ДЛЯ ЧАТОВ ============

    // 1️⃣ Получить все чаты пользователя
    @GetMapping("/sessions")
    public List<ChatSessionDto> getUserSessions(
            @AuthenticationPrincipal UserDetails principal
    ) {
        return chatService.getUserChatSessions(principal.getUsername());
    }

    // 2️⃣ Получить детали конкретного чата
    @GetMapping("/sessions/{sessionId}")
    public ChatSessionDetailDto getSessionDetail(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return chatService.getChatSessionDetail(sessionId, principal.getUsername());
    }

    // 3️⃣ Удалить чат
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        chatService.deleteChatSession(sessionId, principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    // ============ ЭНДПОИНТЫ ДЛЯ СЕКЦИЙ ============

    // 4️⃣ Создать НОВЫЙ чат для секции
    @PostMapping("/sections/{sectionId}/chats")
    public ResponseEntity<ChatSessionDto> createSectionChat(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        User user = userService.findByUsername(principal.getUsername());
        var chat = chatService.createNewSectionChat(user, sectionId);
        return ResponseEntity.ok(convertToDto(chat));
    }

    // 5️⃣ Получить ВСЕ чаты секции
    @GetMapping("/sections/{sectionId}/chats")
    public List<ChatSessionDto> getSectionChats(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal
    ) {
        return chatService.getSectionChats(principal.getUsername(), sectionId);
    }

    // 6️⃣ Отправить сообщение в конкретный чат
    @PostMapping("/chats/{chatId}/messages")
    public ResponseEntity<Void> sendMessage(
            @PathVariable Long chatId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody String message
    ) {
        chatService.addUserMessageAndAiReply(chatId, message);
        return ResponseEntity.ok().build();
    }

    // Вспомогательный метод для конвертации
    private ChatSessionDto convertToDto(com.example.usefy.model.chat.ChatSession session) {
        // Здесь можно использовать сервис, но для простоты вернём базовый DTO
        return new ChatSessionDto(
                session.getId(),
                session.getTitle(),
                session.getCreatedAt(),
                session.getSection() != null ? session.getSection().getId() : null,
                "Чат",
                0,
                ""
        );
    }
}