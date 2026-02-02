package com.example.usefy.repository.course;

import com.example.usefy.model.course.SectionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SectionProgressRepository extends JpaRepository<SectionProgress, Long> {

    Optional<SectionProgress> findByUserUsernameAndSectionId(String username, Long sectionId);

    boolean existsByUserUsernameAndSectionId(String username, Long sectionId);
}
