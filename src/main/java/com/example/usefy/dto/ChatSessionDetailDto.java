package com.example.usefy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatSessionDetailDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private Long sectionId;
    private String sectionTitle;
    private List<ChatMessageDto> messages;
}