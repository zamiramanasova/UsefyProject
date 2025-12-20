package com.example.usefy.model.course;

import com.example.usefy.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "section_progress",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "section_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Section section;

    private boolean completed;
}
