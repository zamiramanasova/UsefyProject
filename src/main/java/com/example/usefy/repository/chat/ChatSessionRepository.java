package com.example.usefy.repository.chat;

import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findByUser(User user);

    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);
}
