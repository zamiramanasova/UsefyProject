package com.example.usefy.model.chat;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст сообщения
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Роль отправителя: USER или AI
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageRole role;

    /**
     * Чат, к которому относится сообщение
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_session_id", nullable = false)
    private ChatSession chatSession;

    /**
     * Время отправки
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
