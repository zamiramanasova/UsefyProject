package com.example.usefy.service.course;

public interface SectionProgressService {

    boolean isSectionCompleted(String username, Long sectionId);

    void markCompleted(String username, Long sectionId);
}
