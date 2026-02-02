package com.example.usefy.repository.chat;

import com.example.usefy.model.chat.ChatMessage;
import com.example.usefy.model.chat.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSessionOrderByCreatedAt(ChatSession chatSession);

    List<ChatMessage> findTop5ByChatSessionOrderByCreatedAtDesc(ChatSession chatSession);
}
