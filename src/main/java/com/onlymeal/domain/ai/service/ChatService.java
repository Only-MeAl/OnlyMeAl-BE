package com.onlymeal.domain.ai.service;

import com.onlymeal.domain.ai.dto.ChatMessage;
import com.onlymeal.domain.ai.dto.ChatRequest;
import com.onlymeal.domain.ai.dto.ChatResponse;
import com.onlymeal.domain.ai.function.FunctionExecutor;
import com.onlymeal.domain.ai.util.PromptBuilder;
import com.onlymeal.domain.rdi.dto.RdiResponse;
import com.onlymeal.domain.rdi.service.RdiService;
import com.onlymeal.domain.user.dao.UserDao;
import com.onlymeal.domain.user.entity.User;
import com.onlymeal.global.ai.GeminiApiClient;
import com.onlymeal.global.exception.CustomException;
import com.onlymeal.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final GeminiApiClient geminiClient;
    private final UserDao userDao;
    private final RdiService rdiService;
    private final ChatSessionService sessionService;
    private final FunctionExecutor functionExecutor;
    private final PromptBuilder promptBuilder;

    @Transactional
    public ChatResponse chat(Long userId, ChatRequest request) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        String sessionId = request.getSessionId();

        sessionService.validateSession(sessionId, userId);

        List<ChatMessage> history = sessionService.getMessages(sessionId);
        history.add(new ChatMessage("user", request.getMessage()));

        String reply = callAiWithHistory(user, history);

        sessionService.saveMessage(sessionId, "user", request.getMessage());
        sessionService.saveMessage(sessionId, "model", reply);

        return new ChatResponse(reply);
    }
    private String callAiWithHistory(User user, List<ChatMessage> history) {
        List<GeminiApiClient.Tool> tools = functionExecutor.defineTools();

        RdiResponse rdi = getRdi(user.getUserId());
        String systemPrompt = promptBuilder.buildChatSystemPrompt(user, rdi);

        List<GeminiApiClient.Content> contents = convertMessages(history);

        final int MAX_FUNCTION_CALLS = 5;
        int functionCallCount = 0;

        while (functionCallCount < MAX_FUNCTION_CALLS) {
            GeminiApiClient.GeminiResponse response = geminiClient.generateChatWithToolsAndSystem(contents, tools, systemPrompt);

            if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
                throw new CustomException(ErrorCode.AI_SERVICE_ERROR);
            }

            List<GeminiApiClient.Part> parts = response.getCandidates().get(0).getContent().getParts();

            List<GeminiApiClient.Part> functionCallParts = parts.stream()
                    .filter(part -> part.getFunctionCall() != null)
                    .toList();

            if (functionCallParts.isEmpty()) {
                return parts.stream()
                        .map(GeminiApiClient.Part::getText)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse("죄송합니다. 답변을 생성할 수 없습니다.");
            }

            log.info("🤖 Function Call 감지: {}개", functionCallParts.size());

            contents.add(new GeminiApiClient.Content("model", parts));

            for (GeminiApiClient.Part fcPart : functionCallParts) {
                GeminiApiClient.FunctionCall functionCall = fcPart.getFunctionCall();
                String functionName = functionCall.getName();
                Map<String, Object> args = functionCall.getArgs() != null ? functionCall.getArgs() : new HashMap<>();

                log.info("🔧 실행: {} args={}", functionName, args);

                Map<String, Object> executionResult = functionExecutor.execute(user.getUserId(), functionName, args);

                contents.add(new GeminiApiClient.Content("user", List.of(
                        new GeminiApiClient.Part(new GeminiApiClient.FunctionResponse(functionName, executionResult))
                )));
            }

            functionCallCount++;
        }

        log.warn("⚠️ Function Call 최대 횟수({}) 초과", MAX_FUNCTION_CALLS);
        return "요청을 처리하는 중 문제가 발생했습니다. 다시 시도해주세요.";
    }

    private List<GeminiApiClient.Content> convertMessages(List<ChatMessage> messages) {
        List<GeminiApiClient.Content> contents = new ArrayList<>();
        for (ChatMessage msg : messages) {
            String role = "user".equalsIgnoreCase(msg.getRole()) ? "user" : "model";
            contents.add(new GeminiApiClient.Content(role, List.of(new GeminiApiClient.Part(msg.getContent()))));
        }
        return contents;
    }

    private RdiResponse getRdi(Long userId) {
        return rdiService.getRdi(userId);
    }
}