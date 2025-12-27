package com.example.usefy.model.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.course.Section;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название диалога (например: "Java basics", "Spring Security help")
     */
    @Column(nullable = false)
    private String title;

    /**
     * Владелец чата
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Сообщения в чате
     */
    @OneToMany(
            mappedBy = "chatSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    /**
     * Дата создания чата
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private Long sectionId;


    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

}
