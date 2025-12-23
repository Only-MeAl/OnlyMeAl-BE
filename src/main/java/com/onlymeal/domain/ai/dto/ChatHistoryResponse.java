package com.onlymeal.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatHistoryResponse {
    private String sessionId;
    private String title;
    private List<ChatMessage> messages;
}