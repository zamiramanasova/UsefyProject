package com.example.usefy.repository.chat;

import com.example.usefy.model.User;
import com.example.usefy.model.chat.ChatSession;
import com.example.usefy.model.course.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    // ============ СТАРЫЕ МЕТОДЫ (оставляем для совместимости) ============

    List<ChatSession> findByUser(User user);

    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);

    // Поиск чата по пользователю и секции (ВОЗВРАЩАЕТ СПИСОК)
    // ВАЖНО: теперь это ВСЕ чаты секции, а не один!
    List<ChatSession> findByUserAndSectionOrderByCreatedAtDesc(User user, Section section);

    // Поиск чата по ID и пользователю (для проверки владельца)
    Optional<ChatSession> findByIdAndUser(Long id, User user);

    // ============ СТАРЫЕ МЕТОДЫ, КОТОРЫЕ УДАЛЯЕМ ============
    // Эти методы больше НЕ ИСПОЛЬЗУЕМ, но пока оставим закомментированными
    /*
    Optional<ChatSession> findByUserAndSectionId(User user, Long sectionId);
    Optional<ChatSession> findByUserAndSection(User user, Section section);
    */

    // ============ НОВЫЕ УДОБНЫЕ МЕТОДЫ ============

    // Найти все чаты пользователя для конкретной секции по ID секции
    @Query("SELECT cs FROM ChatSession cs WHERE cs.user = :user AND cs.section.id = :sectionId ORDER BY cs.createdAt DESC")
    List<ChatSession> findByUserAndSectionIdOrderByCreatedAtDesc(@Param("user") User user, @Param("sectionId") Long sectionId);

    // Проверить, есть ли у пользователя доступ к чату
    default boolean isChatOwnedByUser(Long chatId, User user) {
        return findByIdAndUser(chatId, user).isPresent();
    }
}