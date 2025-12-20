package com.example.usefy.model.course;

import com.example.usefy.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_course_progress",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "section_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(nullable = false)
    private boolean completed;

    private LocalDateTime completedAt;
}
