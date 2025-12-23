package com.onlymeal.domain.ai.dao;

import com.onlymeal.domain.ai.entity.ChatMessageEntity;
import com.onlymeal.domain.ai.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatDao {

    void createSession(ChatSession session);

    ChatSession getSession(@Param("sessionId") String sessionId);

    List<ChatSession> getSessionsByUser(@Param("userId") Long userId);

    void updateSessionTitle(@Param("sessionId") String sessionId, @Param("title") String title);

    void deleteSession(@Param("sessionId") String sessionId);

    void saveMessage(ChatMessageEntity message);

    List<ChatMessageEntity> getMessages(@Param("sessionId") String sessionId);

    void deleteMessages(@Param("sessionId") String sessionId);
}