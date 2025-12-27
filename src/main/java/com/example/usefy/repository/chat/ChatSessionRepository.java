package com.example.usefy.repository.chat;

import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.User;
import com.example.usefy.model.course.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findByUser(User user);

    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);

    Optional<ChatSession> findByUserAndSectionId(User user, Long sectionId);

    Optional<ChatSession> findByUserAndSection(User user, Section section);


}
