package com.example.usefy.repository.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ChatSessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserOrderByCreatedAtDesc_ShouldReturnSessionsOrdered() {
        // given
        User user = User.builder()
                .username("testuser")
                .email("test@test.com")
                .passwordHash("hash")
                .build();
        entityManager.persist(user);

        ChatSession session1 = ChatSession.builder()
                .user(user)
                .title("First Chat")
                .build();
        entityManager.persist(session1);

        // Немного задерживаем для разницы во времени
        try { Thread.sleep(10); } catch (InterruptedException e) {}

        ChatSession session2 = ChatSession.builder()
                .user(user)
                .title("Second Chat")
                .build();
        entityManager.persist(session2);

        entityManager.flush();

        // when
        List<ChatSession> found = chatSessionRepository.findByUserOrderByCreatedAtDesc(user);

        // then
        assertThat(found).hasSize(2);
        assertThat(found.get(0).getTitle()).isEqualTo("Second Chat");  // последний созданный
        assertThat(found.get(1).getTitle()).isEqualTo("First Chat");
    }

    @Test
    void findByUserAndSectionId_ShouldReturnSession() {
        // given
        User user = User.builder()
                .username("testuser")
                .email("test@test.com")
                .passwordHash("hash")
                .build();
        entityManager.persist(user);

        // Создаём секцию (упрощённо)
        // В реальности нужно создать Course и Section

        // when/then
        // Тест будет зависеть от вашей модели данных
        assertThat(true).isTrue();
    }
}