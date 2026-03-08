package com.example.usefy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private String content;
    private String role; // "USER" или "AI"
    private LocalDateTime createdAt;
}