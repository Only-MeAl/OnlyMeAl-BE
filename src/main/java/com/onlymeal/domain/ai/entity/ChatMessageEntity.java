package com.onlymeal.domain.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageEntity {
    private Long messageId;
    private String sessionId;
    private String role;
    private String content;
    private LocalDateTime createdAt;
}