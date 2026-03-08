package com.example.usefy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatSessionDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private Long sectionId;
    private String sectionTitle;
    private int messageCount;
    private String lastMessagePreview;
}