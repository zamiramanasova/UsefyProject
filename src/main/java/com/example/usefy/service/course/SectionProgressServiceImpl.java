package com.example.usefy.service.course;

import com.example.usefy.model.User;
import com.example.usefy.model.course.Section;
import com.example.usefy.model.course.SectionProgress;
import com.example.usefy.repository.UserRepository;
import com.example.usefy.repository.course.SectionProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectionProgressServiceImpl implements SectionProgressService {

    private final SectionProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final SectionProgressRepository sectionRepository;

    @Override
    public boolean isSectionCompleted(String username, Long sectionId) {
        return progressRepository.existsByUserUsernameAndSectionId(username, sectionId);
    }

    @Override
    public void markCompleted(String username, Long sectionId) {

        if (isSectionCompleted(username, sectionId)) return;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found")).getSection();

        SectionProgress progress = SectionProgress.builder()

                .user(user)
                .section(section)
                .completed(true)
                .build();

        progressRepository.save(progress);
    }
}
