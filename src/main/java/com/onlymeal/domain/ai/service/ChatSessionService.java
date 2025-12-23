package com.onlymeal.domain.ai.service;

import com.onlymeal.domain.ai.dao.ChatDao;
import com.onlymeal.domain.ai.dto.ChatHistoryResponse;
import com.onlymeal.domain.ai.dto.ChatMessage;
import com.onlymeal.domain.ai.dto.ChatSessionResponse;
import com.onlymeal.domain.ai.entity.ChatMessageEntity;
import com.onlymeal.domain.ai.entity.ChatSession;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatSessionService {

    private final ChatDao chatDao;

    public String createSession(Long userId, String firstMessage) {
        String sessionId = UUID.randomUUID().toString();
        ChatSession newSession = ChatSession.builder()
                .sessionId(sessionId)
                .userId(userId)
                .title(truncateTitle(firstMessage))
                .build();
        chatDao.createSession(newSession);
        log.info("🆕 새 세션 생성: {}", sessionId);
        return sessionId;
    }

    public void validateSession(String sessionId, Long userId) {
        ChatSession session = chatDao.getSession(sessionId);
        if (session == null) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }
        if (!session.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public List<ChatMessage> getMessages(String sessionId) {
        return chatDao.getMessages(sessionId).stream()
                .map(e -> new ChatMessage(e.getRole(), e.getContent()))
                .collect(Collectors.toList());
    }

    public void saveMessage(String sessionId, String role, String content) {
        chatDao.saveMessage(ChatMessageEntity.builder()
                .sessionId(sessionId)
                .role(role)
                .content(content)
                .build());
    }

    public List<ChatSessionResponse> getSessions(Long userId) {
        return chatDao.getSessionsByUser(userId).stream()
                .map(ChatSessionResponse::from)
                .collect(Collectors.toList());
    }

    public ChatHistoryResponse getSessionHistory(Long userId, String sessionId) {
        ChatSession session = chatDao.getSession(sessionId);
        if (session == null) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }
        if (!session.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<ChatMessage> messages = getMessages(sessionId);
        return new ChatHistoryResponse(sessionId, session.getTitle(), messages);
    }

    public void deleteSession(Long userId, String sessionId) {
        validateSession(sessionId, userId);
        chatDao.deleteSession(sessionId);

        log.info("🗑️ 세션 삭제: {}", sessionId);
    }

    private String truncateTitle(String message) {
        if (message == null) return "새 대화";
        return message.length() > 50 ? message.substring(0, 50) + "..." : message;
    }
}