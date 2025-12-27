package com.example.usefy.web;

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
    public void ask(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody String question
    ) {
        User user = userService.findByUsername(principal.getUsername());
        var chat = chatService.getOrCreateSectionChat(user, sectionId);

        chatService.addUserMessageAndAiReply(chat.getId(), question);
    }
}
