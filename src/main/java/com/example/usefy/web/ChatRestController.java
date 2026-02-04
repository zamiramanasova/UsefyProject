package com.example.usefy.web.api;

import com.example.usefy.model.User;
import com.example.usefy.service.UserService;
import com.example.usefy.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/{sectionId}")
    public void sendMessage(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody String message

    ) {
        var user = userService.findByUsername(principal.getUsername());
        var chat = chatService.getOrCreateSectionChat(user, sectionId);

        chatService.addUserMessageAndAiReply(chat.getId(), message);
    }
}
