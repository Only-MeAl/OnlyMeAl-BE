package com.onlymeal.domain.ai.dto;

import com.onlymeal.domain.ai.entity.ChatSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatSessionResponse {
    private String sessionId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatSessionResponse from(ChatSession session) {
        return new ChatSessionResponse(
                session.getSessionId(),
                session.getTitle(),
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }
}