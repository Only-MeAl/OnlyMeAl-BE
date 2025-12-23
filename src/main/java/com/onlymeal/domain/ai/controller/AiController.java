package com.onlymeal.domain.ai.controller;

import com.onlymeal.domain.ai.dto.*;
import com.onlymeal.domain.ai.service.ChatService;
import com.onlymeal.domain.ai.service.ChatSessionService;
import com.onlymeal.domain.ai.service.FoodRecognitionService;
import com.onlymeal.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final FoodRecognitionService foodRecognitionService;
    private final ChatService chatService;
    private final ChatSessionService chatSessionService;

    @PostMapping("/recognize-food")
    public ApiResponse<FoodRecognitionResponse> recognizeFood(@RequestParam("image") MultipartFile file) {
        FoodRecognitionResponse response = foodRecognitionService.recognizeFood(file);
        return ApiResponse.success(response);
    }

    @PostMapping("/chat/session")
    public ApiResponse<String> createChatSession(@AuthenticationPrincipal Long userId) {
        String sessionId = chatSessionService.createSession(userId, null);
        return ApiResponse.success(sessionId);
    }

    @PostMapping("/chat")
    public ApiResponse<ChatResponse> chat(@AuthenticationPrincipal Long userId,
                                          @RequestBody @Valid ChatRequest request) {
        ChatResponse response = chatService.chat(userId, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/chat/sessions")
    public ApiResponse<List<ChatSessionResponse>> getSessions(@AuthenticationPrincipal Long userId) {
        List<ChatSessionResponse> sessions = chatSessionService.getSessions(userId);
        return ApiResponse.success(sessions);
    }

    @GetMapping("/chat/sessions/{sessionId}")
    public ApiResponse<ChatHistoryResponse> getSessionHistory(@AuthenticationPrincipal Long userId,
                                                              @PathVariable String sessionId) {
        ChatHistoryResponse history = chatSessionService.getSessionHistory(userId, sessionId);
        return ApiResponse.success(history);
    }

    @DeleteMapping("/chat/sessions/{sessionId}")
    public ApiResponse<Void> deleteSession(@AuthenticationPrincipal Long userId,
                                           @PathVariable String sessionId) {
        chatSessionService.deleteSession(userId, sessionId);
        return ApiResponse.success(null);
    }
}